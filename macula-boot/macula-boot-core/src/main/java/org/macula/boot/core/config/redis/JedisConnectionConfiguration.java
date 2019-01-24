/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.macula.boot.core.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Redis connection configuration using Jedis.
 *
 * @author Mark Paluch
 * @author Stephane Nicoll
 */
public class JedisConnectionConfiguration extends RedisConnectionConfiguration {

    private final RedisProperties properties;

    public JedisConnectionConfiguration(RedisProperties properties) {
        super(properties);
        this.properties = properties;
    }

    public JedisConnectionFactory createJedisConnectionFactory() {
        JedisClientConfiguration clientConfiguration = getJedisClientConfiguration();
        if (getSentinelConfig() != null) {
            return new JedisConnectionFactory(getSentinelConfig(), clientConfiguration);
        }
        if (getClusterConfiguration() != null) {
            return new JedisConnectionFactory(getClusterConfiguration(), clientConfiguration);
        }
        return new JedisConnectionFactory(getStandaloneConfig(), clientConfiguration);
    }

    private JedisClientConfiguration getJedisClientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = applyProperties(JedisClientConfiguration.builder());
        RedisProperties.Pool pool = this.properties.getJedis().getPool();
        if (pool != null) {
            applyPooling(pool, builder);
        }
        if (StringUtils.hasText(this.properties.getUrl())) {
            customizeConfigurationFromUrl(builder);
        }
        return builder.build();
    }

    private JedisClientConfiguration.JedisClientConfigurationBuilder applyProperties(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        if (this.properties.isSsl()) {
            builder.useSsl();
        }
        if (this.properties.getTimeout() != null) {
            Duration timeout = this.properties.getTimeout();
            builder.readTimeout(timeout).connectTimeout(timeout);
        }
        return builder;
    }

    private void applyPooling(RedisProperties.Pool pool, JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        builder.usePooling().poolConfig(jedisPoolConfig(pool));
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }

    private void customizeConfigurationFromUrl(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        ConnectionInfo connectionInfo = parseUrl(this.properties.getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }
    }
}
