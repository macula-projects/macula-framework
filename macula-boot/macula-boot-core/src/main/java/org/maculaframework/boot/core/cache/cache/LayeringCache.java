/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maculaframework.boot.core.cache.cache;

import org.maculaframework.boot.core.cache.listener.RedisPubSubMessage;
import org.maculaframework.boot.core.cache.listener.RedisPubSubMessageType;
import org.maculaframework.boot.core.cache.listener.RedisPublisher;
import org.maculaframework.boot.core.cache.setting.LayeringCacheSetting;
import org.maculaframework.boot.core.cache.stats.CacheStats;
import org.maculaframework.boot.core.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.concurrent.Callable;

/**
 * 多级缓存
 *
 * @author yuhao.wang
 */
public class LayeringCache extends AbstractValueAdaptingCache {
    Logger logger = LoggerFactory.getLogger(LayeringCache.class);

    /**
     * redis 客户端
     */
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 一级缓存
     */
    private AbstractValueAdaptingCache firstCache;

    /**
     * 二级缓存
     */
    private AbstractValueAdaptingCache secondCache;

    /**
     * 多级缓存配置
     */
    private LayeringCacheSetting layeringCacheSetting;

    /**
     * 是否使用一级缓存， 默认true
     */
    private boolean useFirstCache = true;

    /**
     * 创建一个多级缓存对象
     *
     * @param redisTemplate        redisTemplate
     * @param firstCache           一级缓存
     * @param secondCache          二级缓存
     * @param stats                是否开启统计
     * @param layeringCacheSetting 多级缓存配置
     */
    public LayeringCache(RedisTemplate<String, Object> redisTemplate, AbstractValueAdaptingCache firstCache,
                         AbstractValueAdaptingCache secondCache, boolean stats, LayeringCacheSetting layeringCacheSetting) {
        this(redisTemplate, firstCache, secondCache, true, stats, secondCache.getName(), layeringCacheSetting);
    }

    /**
     * @param redisTemplate        redisTemplate
     * @param firstCache           一级缓存
     * @param secondCache          二级缓存
     * @param useFirstCache        是否使用一级缓存，默认是
     * @param stats                是否开启统计，默认否
     * @param name                 缓存名称
     * @param layeringCacheSetting 多级缓存配置
     */
    public LayeringCache(RedisTemplate<String, Object> redisTemplate, AbstractValueAdaptingCache firstCache,
                         AbstractValueAdaptingCache secondCache, boolean useFirstCache, boolean stats, String name, LayeringCacheSetting layeringCacheSetting) {
        super(stats, name);
        this.redisTemplate = redisTemplate;
        this.firstCache = firstCache;
        this.secondCache = secondCache;
        this.useFirstCache = useFirstCache;
        this.layeringCacheSetting = layeringCacheSetting;
    }

    @Override
    public LayeringCache getNativeCache() {
        return this;
    }

    @Override
    public Object get(Object key) {
        Object result = null;
        if (useFirstCache) {
            result = firstCache.get(key);
            logger.debug("查询一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
        }
        if (result == null) {
            result = secondCache.get(key);
            firstCache.putIfAbsent(key, result);
            logger.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
        }
        return fromStoreValue(result);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        if (useFirstCache) {
            T result = firstCache.get(key, type);
            logger.debug("查询一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
            if (result != null) {
                return fromStoreValue(result);
            }
        }

        T result = secondCache.get(key, type);
        firstCache.putIfAbsent(key, result);
        logger.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (useFirstCache) {
            Object result = firstCache.get(key);
            logger.debug("查询一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
            if (result != null) {
                return (T) fromStoreValue(result);
            }
        }
        T result = secondCache.get(key, valueLoader);
        firstCache.putIfAbsent(key, result);
        logger.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JSONUtils.objectToJson(result));
        return result;
    }

    @Override
    public void put(Object key, Object value) {
        secondCache.put(key, value);
        // 删除一级缓存
        if (useFirstCache) {
            deleteFirstCache(key);
        }
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Object result = secondCache.putIfAbsent(key, value);
        // 删除一级缓存
        if (useFirstCache) {
            deleteFirstCache(key);
        }
        return result;
    }

    @Override
    public void evict(Object key) {
        // 删除的时候要先删除二级缓存再删除一级缓存，否则有并发问题
        secondCache.evict(key);
        // 删除一级缓存
        if (useFirstCache) {
            deleteFirstCache(key);
        }
    }

    @Override
    public void clear() {
        // 删除的时候要先删除二级缓存再删除一级缓存，否则有并发问题
        secondCache.clear();
        if (useFirstCache) {
            // 清除一级缓存需要用到redis的订阅/发布模式，否则集群中其他服服务器节点的一级缓存数据无法删除
            RedisPubSubMessage message = new RedisPubSubMessage();
            message.setCacheName(getName());
            message.setMessageType(RedisPubSubMessageType.CLEAR);
            // 发布消息
            RedisPublisher.publisher(redisTemplate, new ChannelTopic(getName()), message);
        }
    }

    private void deleteFirstCache(Object key) {
        // 删除一级缓存需要用到redis的Pub/Sub（订阅/发布）模式，否则集群中其他服服务器节点的一级缓存数据无法删除
        RedisPubSubMessage message = new RedisPubSubMessage();
        message.setCacheName(getName());
        message.setKey(key);
        message.setMessageType(RedisPubSubMessageType.EVICT);
        // 发布消息
        RedisPublisher.publisher(redisTemplate, new ChannelTopic(getName()), message);
    }

    /**
     * 获取一级缓存
     *
     * @return FirstCache
     */
    public Cache getFirstCache() {
        return firstCache;
    }

    /**
     * 获取二级缓存
     *
     * @return SecondCache
     */
    public Cache getSecondCache() {
        return secondCache;
    }

    @Override
    public CacheStats getCacheStats() {
        CacheStats cacheStats = new CacheStats();
        cacheStats.addCacheRequestCount(firstCache.getCacheStats().getCacheRequestCount().longValue());
        cacheStats.addCachedMethodRequestCount(secondCache.getCacheStats().getCachedMethodRequestCount().longValue());
        cacheStats.addCachedMethodRequestTime(secondCache.getCacheStats().getCachedMethodRequestTime().longValue());

        firstCache.getCacheStats().addCachedMethodRequestCount(secondCache.getCacheStats().getCacheRequestCount().longValue());

        setCacheStats(cacheStats);
        return cacheStats;
    }

    public LayeringCacheSetting getLayeringCacheSetting() {
        return layeringCacheSetting;
    }

    @Override
    public boolean isAllowNullValues() {
        return secondCache.isAllowNullValues();
    }
}
