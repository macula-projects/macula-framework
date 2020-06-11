/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.core.config;

import org.maculaframework.boot.core.redis.KryoRedisSerializer;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * <b>DataRedisConfiguration</b> Spring Data Redis的存取模板定义
 * </p>
 *
 * @author Rain
 * @since 2020-04-03
 */

@Configuration
public class DataRedisConfiguration {
    @Bean(name = "dataRedisConnectionFactory")
    @ConditionalOnBean(name = "dataRedissonClient")
    @ConditionalOnMissingBean(name = "dataRedisConnectionFactory")
    public RedisConnectionFactory dataRedisConnectionFactory(@Qualifier("dataRedissonClient") RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnBean(name = "dataRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "dataRedisTemplate")
    public RedisTemplate<String, Object> dataRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        // TODO 读取配置好点（Class）kryo.register
        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(new Class<?> [] { Object.class});

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(dataRedisConnectionFactory);

        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        template.setValueSerializer(kryoRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        return template;
    }

    @Bean(name = "stringRedisTemplate")
    @ConditionalOnBean(name = "dataRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "stringDataRedisTemplate")
    public StringRedisTemplate dataStringRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(dataRedisConnectionFactory);
        return template;
    }
}
