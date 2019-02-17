package org.macula.boot.core.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.macula.boot.core.config.core.CoreConfigProperties;
import org.macula.boot.core.exception.handler.ServiceExceptionAspect;
import org.macula.boot.core.exception.translator.HibernateExceptionTranslator;
import org.macula.boot.core.exception.translator.PersistenceExceptionTranslator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * <p>
 * <b>CoreAutoConfiguration</b> Core模块的自动配置入口
 * 在系统数据源之前配置时，系统默认的JPA、JDBC等都会靠后
 * </p>
 *
 * @author Rain
 * @since 2019-01-22
 */

@Configuration
@EnableConfigurationProperties({CoreConfigProperties.class})
@EnableRedisRepositories
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class, RedisAutoConfiguration.class})
@Import({RedisConfiguration.class, DataSourceConfiguration.class})
public class CoreAutoConfiguration {

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new PersistenceExceptionTranslator();
    }

    @Bean
    public ServiceExceptionAspect serviceExceptionAspect() {
        return new ServiceExceptionAspect();
    }

}
