package org.macula.boot.core.cache.config;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Data;
import org.macula.boot.core.redis.KryoRedisSerializer;
import org.macula.boot.core.redis.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@TestConfiguration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {

    private int database = 0;
    private String host = "127.0.0.1";
    private String password;
    private int port = 6379;
    private int maxIdle = 200;
    private int minIdle = 10;
    private int maxActive = 80;
    private int maxWait = -1;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        ClientResources clientResources = DefaultClientResources.create();
        LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
        builder.clientResources(clientResources);
        LettuceClientConfiguration configuration = builder.build();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(database);
        return new LettuceConnectionFactory(config, configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(Object.class);

        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        redisTemplate.setValueSerializer(kryoRedisSerializer);
        redisTemplate.setHashValueSerializer(kryoRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
