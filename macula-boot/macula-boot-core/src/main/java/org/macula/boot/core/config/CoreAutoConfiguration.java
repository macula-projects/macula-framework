package org.macula.boot.core.config;

import org.macula.boot.core.config.core.CoreConfigProperties;
import org.macula.boot.core.config.json.MaculaJackson2ObjectMapperBuilderCustomizer;
import org.macula.boot.exception.handler.ServiceExceptionAspect;
import org.macula.boot.exception.translator.HibernateExceptionTranslator;
import org.macula.boot.exception.translator.PersistenceExceptionTranslator;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@AutoConfigureBefore({RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@AutoConfigureAfter({JacksonAutoConfiguration.class})
@Import({RedisConfiguration.class, DataSourceConfiguration.class, JpaRepositoriesConfiguration.class})
public class CoreAutoConfiguration implements ApplicationContextAware {

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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new MaculaJackson2ObjectMapperBuilderCustomizer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        org.macula.boot.ApplicationContext.setContainer(applicationContext);
    }
}
