package org.macula.boot.core.config;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.macula.boot.core.cache.aspect.LayeringAspect;
import org.macula.boot.core.cache.manager.CacheManager;
import org.macula.boot.core.cache.manager.LayeringCacheManager;
import org.macula.boot.core.config.redis.CacheRedisProperties;
import org.macula.boot.core.config.redis.DataRedisProperties;
import org.macula.boot.core.config.redis.LettuceConnectionConfiguration;
import org.macula.boot.core.redis.KryoRedisSerializer;
import org.macula.boot.core.redis.StringRedisSerializer;
import org.macula.boot.core.repository.templatequery.template.FreemarkerSqlTemplates;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * <p>
 * <b>CoreAutoConfiguration</b> Core模块的自动配置入口
 * </p>
 *
 * @author Rain
 * @since 2019-01-22
 */

@Configuration
@EnableConfigurationProperties({CoreConfigProperties.class, DataRedisProperties.class, CacheRedisProperties.class})
@EnableRedisRepositories
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class CoreAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    // 缓存配置
    @Bean(name = "cacheRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "cacheRedisConnectionFactory")
    public RedisConnectionFactory cacheRedisConnectionFactory(ClientResources clientResources, CacheRedisProperties cacheRedisProperties) {
        LettuceConnectionConfiguration lettuceCfg = new LettuceConnectionConfiguration(cacheRedisProperties);

        LettuceClientConfiguration clientConfig = lettuceCfg.getLettuceClientConfiguration(clientResources, cacheRedisProperties.getLettuce().getPool());
        return lettuceCfg.createLettuceConnectionFactory(clientConfig);
    }

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

    // REDIS读写配置
    @Bean(name = "dataRedisConnectionFactory")
    @ConditionalOnMissingBean(name = "dataRedisConnectionFactory")
    public RedisConnectionFactory dataRedisConnectionFactory(ClientResources clientResources, DataRedisProperties dataRedisProperties) {
        LettuceConnectionConfiguration lettuceCfg = new LettuceConnectionConfiguration(dataRedisProperties);

        LettuceClientConfiguration clientConfig = lettuceCfg.getLettuceClientConfiguration(clientResources, dataRedisProperties.getLettuce().getPool());
        return lettuceCfg.createLettuceConnectionFactory(clientConfig);
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "dataRedisTemplate")
    public RedisTemplate<String, Object> dataRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        KryoRedisSerializer<Object> kryoRedisSerializer = new KryoRedisSerializer<>(Object.class);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(dataRedisConnectionFactory);

        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        template.setValueSerializer(kryoRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean(name = "stringRedisTemplate")
    @ConditionalOnMissingBean(name = "stringDataRedisTemplate")
    public StringRedisTemplate dataStringRedisTemplate(@Qualifier("dataRedisConnectionFactory") RedisConnectionFactory dataRedisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(dataRedisConnectionFactory);
        return template;
    }

    @Bean
    public FreemarkerSqlTemplates freemarkerSqlTemplates() {
        return new FreemarkerSqlTemplates();
    }
}
