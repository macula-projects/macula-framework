package org.macula.boot.core.cache.manager;

import org.macula.boot.core.cache.cache.Cache;
import org.macula.boot.core.cache.cache.LayeringCache;
import org.macula.boot.core.cache.cache.caffeine.CaffeineCache;
import org.macula.boot.core.cache.cache.redis.RedisCache;
import org.macula.boot.core.cache.setting.LayeringCacheSetting;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yuhao.wang
 */
public class LayeringCacheManager extends AbstractCacheManager {
    public LayeringCacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        cacheManagers.add(this);
    }

    @Override
    protected Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, layeringCacheSetting.getFirstCacheSetting(), getStats());
        // 创建二级缓存
        RedisCache redisCache = new RedisCache(name, redisTemplate, layeringCacheSetting.getSecondaryCacheSetting(), getStats());
        return new LayeringCache(redisTemplate, caffeineCache, redisCache, super.getStats(), layeringCacheSetting);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
