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

package org.maculaframework.boot.core.cache.cache.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * redis key 生成
 *
 * @author yuhao.wang3
 */
public class RedisCacheKey {

    /**
     * 前缀序列化器
     */
    private final RedisSerializer<String> prefixSerializer1 = new StringRedisSerializer();

    /**
     * 缓存key
     */
    private final Object keyElement;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 是否使用缓存前缀
     */
    private boolean usePrefix = true;

    /**
     * RedisTemplate 的key序列化器
     */
    private final RedisSerializer<Object> serializer;

    /**
     * @param keyElement 缓存key
     * @param serializer RedisSerializer
     */
    @SuppressWarnings("unchecked")
    public RedisCacheKey(Object keyElement, RedisSerializer<?> serializer) {

        Assert.notNull(keyElement, "缓存key不能为NULL");
        Assert.notNull(serializer, "key的序列化器不能为NULL");
        this.keyElement = keyElement;
        this.serializer = (RedisSerializer<Object>) serializer;
    }

    /**
     * 获取缓存key
     *
     * @return String
     */
    public String getKey() {

        return new String(getKeyBytes());
    }

    /**
     * 获取key的byte数组
     *
     * @return byte[]
     */
    public byte[] getKeyBytes() {

        byte[] rawKey = serializeKeyElement();
        if (!usePrefix) {
            return rawKey;
        }
        byte[] prefix = getPrefix();
        byte[] prefixedKey = Arrays.copyOf(prefix, prefix.length + rawKey.length);
        System.arraycopy(rawKey, 0, prefixedKey, prefix.length, rawKey.length);

        return prefixedKey;
    }

    private byte[] serializeKeyElement() {

        if (serializer == null && keyElement instanceof byte[]) {
            return (byte[]) keyElement;
        }

        return serializer.serialize(keyElement);
    }

    /**
     * 获取缓存前缀，默认缓存前缀是":"
     *
     * @return byte[]
     */
    public byte[] getPrefix() {
        return prefixSerializer1.serialize((StringUtils.isEmpty(cacheName) ? cacheName.concat(":") : cacheName.concat(":")));
    }

    /**
     * 设置缓存名称
     *
     * @param cacheName cacheName
     * @return RedisCacheKey
     */
    public RedisCacheKey cacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    /**
     * 设置是否使用缓存前缀，默认使用
     *
     * @param usePrefix usePrefix
     * @return RedisCacheKey
     */
    public RedisCacheKey usePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
        return this;
    }

}