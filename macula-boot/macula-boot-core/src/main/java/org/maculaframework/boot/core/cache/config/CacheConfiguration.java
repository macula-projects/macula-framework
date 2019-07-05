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

package org.maculaframework.boot.core.cache.config;

import org.maculaframework.boot.core.cache.aspect.LayeringAspect;
import org.maculaframework.boot.core.cache.manager.CacheManager;
import org.maculaframework.boot.core.cache.manager.LayeringCacheManager;
import org.maculaframework.boot.core.redis.KryoRedisSerializer;
import org.maculaframework.boot.core.redis.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * <b>CacheConfiguration</b> 缓存配置
 * </p>
 *
 * @author Rain
 * @since 2019-03-11
 */

@Configuration
public class CacheConfiguration {
    @Bean(name = "cacheRedisTemplate")
    @ConditionalOnMissingBean(name = "cacheRedisTemplate")
    public RedisTemplate<String, Object> cacheRedisTemplate(@Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory cacheRedisConnectionFactory) {
        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(Object.class);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cacheRedisConnectionFactory);
        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        template.setValueSerializer(kryoRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // 开启统计功能
        layeringCacheManager.setStats(true);
        return layeringCacheManager;
    }

    @Bean
    public LayeringAspect layeringAspect() {
        return new LayeringAspect();
    }
}
