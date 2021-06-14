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
import org.maculaframework.boot.core.redis.FSTRedisSerializer;
import org.maculaframework.boot.core.redis.KryoRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

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
    @Bean
    @ConditionalOnMissingBean(name = "cacheRedissonClient")
    public RedissonClient cacheRedissonClient() {
        // 当什么都没有配置时默认连接本地redis
        Config config = new Config();
        config.useSingleServer()
            .setTimeout(1000000)
            .setAddress("redis://127.0.0.1:6379");
        config.setCodec(new FstCodec());
        return Redisson.create(config);
    }

    @Bean(name = "cacheRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "cacheRedisConnectionFactory")
    public RedisConnectionFactory cacheRedisConnectionFactory(@Qualifier("cacheRedissonClient") RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean(name = "cacheRedisTemplate")
    @ConditionalOnMissingBean(name = "cacheRedisTemplate")
    public RedisTemplate<String, Object> cacheRedisTemplate(@Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory cacheRedisConnectionFactory) {

        // TODO 建议采用配置方式
        // RedisSerializer<Object> redisSerializer = new KryoRedisSerializer<>(new Class<?>[] {Object.class});
        RedisSerializer<Object> redisSerializer = new FSTRedisSerializer<>();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cacheRedisConnectionFactory);
        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
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
