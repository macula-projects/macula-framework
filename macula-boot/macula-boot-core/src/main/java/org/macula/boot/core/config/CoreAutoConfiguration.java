package org.macula.boot.core.config;

import org.macula.boot.core.cache.aspect.LayeringAspect;
import org.macula.boot.core.cache.manager.CacheManager;
import org.macula.boot.core.cache.manager.LayeringCacheManager;
import org.macula.boot.core.config.redis.CacheRedisProperties;
import org.macula.boot.core.config.redis.DataRedisProperties;
import org.macula.boot.core.config.redis.JedisConnectionConfiguration;
import org.macula.boot.core.redis.KryoRedisSerializer;
import org.macula.boot.core.redis.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;

import java.net.UnknownHostException;

/**
 * <p>
 * <b>CoreAutoConfiguration</b> Core模块的自动配置入口
 * </p>
 *
 * @since 2019-01-22
 * @author Rain
 */

@Configuration
@EnableConfigurationProperties({CoreConfigProperties.class, DataRedisProperties.class, CacheRedisProperties.class})
@EnableRedisRepositories()
@Import({JedisConnectionConfiguration.class})
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class CoreAutoConfiguration {

    // 缓存配置
    @Bean(name = "cacheRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "cacheRedisConnectionFactory")
    public RedisConnectionFactory cacheRedisConnectionFactory(CacheRedisProperties cacheRedisProperties) {
        JedisConnectionConfiguration jedisCfg = new JedisConnectionConfiguration(cacheRedisProperties);
        return jedisCfg.createJedisConnectionFactory();
    }

    @Bean(name = "cacheRedisTemplate")
    @ConditionalOnMissingBean(name = "cacheRedisTemplate")
    public RedisTemplate<Object, Object> cacheRedisTemplate(@Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory cacheRedisConnectionFactory) {
        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(Object.class);

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
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

    // REDIS读写配置
    @Bean(name = "dataRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "dataRedisConnectionFactory")
    public RedisConnectionFactory dataRedisConnectionFactory(DataRedisProperties dataRedisProperties) {
        JedisConnectionConfiguration jedisCfg = new JedisConnectionConfiguration(dataRedisProperties);
        return jedisCfg.createJedisConnectionFactory();
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "dataRedisTemplate")
    public RedisTemplate<Object, Object> dataRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(Object.class);

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(dataRedisConnectionFactory);

        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        template.setValueSerializer(kryoRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean(name = "stringDataRedisTemplate")
    @ConditionalOnMissingBean(name = "stringDataRedisTemplate")
    public StringRedisTemplate dataStringRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(dataRedisConnectionFactory);
        return template;
    }
}
