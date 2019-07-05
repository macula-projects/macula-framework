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

package org.maculaframework.boot.core.config;

import org.maculaframework.boot.core.cache.config.CacheConfiguration;
import org.maculaframework.boot.core.config.core.CoreConfigProperties;
import org.maculaframework.boot.core.config.json.MaculaJackson2ObjectMapperBuilderCustomizer;
import org.maculaframework.boot.core.exception.config.ExceptionConfiguration;
import org.maculaframework.boot.core.klock.config.KlockConfiguration;
import org.maculaframework.boot.core.modelmapper.config.ModelMapperConfiguration;
import org.maculaframework.boot.core.uid.config.UidConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
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
@AutoConfigureAfter({JacksonAutoConfiguration.class})
@AutoConfigureBefore({MessageSourceAutoConfiguration.class, RedisAutoConfiguration.class,
        DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, })
@Import({MessageSourceConfiguration.class, ExceptionConfiguration.class, UidConfiguration.class,
        RedisConfiguration.class, CacheConfiguration.class, KlockConfiguration.class,
        DataSourceConfiguration.class, JpaRepositoriesConfiguration.class, ModelMapperConfiguration.class})
public class CoreAutoConfiguration implements ApplicationContextAware {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new MaculaJackson2ObjectMapperBuilderCustomizer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        org.maculaframework.boot.ApplicationContext.setContainer(applicationContext);
    }
}

