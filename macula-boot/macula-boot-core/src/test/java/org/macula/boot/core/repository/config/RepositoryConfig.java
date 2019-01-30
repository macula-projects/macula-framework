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

package org.macula.boot.core.repository.config;

import org.macula.boot.core.domain.support.AuditorAwareStub;
import org.macula.boot.core.domain.support.DbDateTimeProvider;
import org.macula.boot.core.repository.MaculaJpaRepositoryFactoryBean;
import org.macula.boot.core.repository.templatequery.template.FreemarkerSqlTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p>
 * <b>RepositoryConfig</b> Spring Data Jpa扩展测试
 * </p>
 *
 * @author Rain
 * @since 2019-01-28
 */

@TestConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.macula.boot.core.repository.support",
        repositoryFactoryBeanClass = MaculaJpaRepositoryFactoryBean.class,
        entityManagerFactoryRef = "maculaEntityManagerFactory")
@EnableJpaAuditing(auditorAwareRef = "auditorAwareStub", dateTimeProviderRef = "dbDateTimeProvider")
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration.class)
public class RepositoryConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Autowired
    private DataSource dataSource;

    @Bean(name = "maculaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean maculaEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .properties(getVendorProperties())
                .packages("org.macula.boot.core.repository.support.domain") //设置实体类所在位置
                .persistenceUnit("maculaPersistenceUnit")
                .build();
    }

    @Bean
    public AuditorAwareStub auditorAwareStub() {
        return new AuditorAwareStub();
    }

    @Bean
    public DbDateTimeProvider dbDateTimeProvider() {
        return new DbDateTimeProvider();
    }

    @Bean
    public FreemarkerSqlTemplates freemarkerSqlTemplates() {
        return new FreemarkerSqlTemplates();
    }

    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }
}
