package org.macula.boot.core.cache.config;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Data;
import org.macula.boot.core.cache.aspect.LayeringAspect;
import org.macula.boot.core.cache.manager.CacheManager;
import org.macula.boot.core.cache.manager.LayeringCacheManager;
import org.macula.boot.core.cache.support.test.TestService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Data
@TestConfiguration
@EnableAspectJAutoProxy
@ConfigurationProperties(prefix = "spring.redis")
public class CacheConfig {
    private int database = 0;
    private String host = "127.0.0.1";
    private String password;
    private int port = 6379;
    private int maxIdle = 200;
    private int minIdle = 10;
    private int maxActive = 80;
    private int maxWait = -1;

    @Bean(name = "cacheRedisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory() {
        ClientResources clientResources = DefaultClientResources.create();
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
        builder.clientResources(clientResources);
        LettuceClientConfiguration configuration = builder.build();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(database);
        return new LettuceConnectionFactory(config, configuration);
    }
}
