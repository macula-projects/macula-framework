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

package org.maculaframework.boot.core.config;

import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.config.jpa.MaculaJpaRepositoriesConfigurationRegistar;
import org.maculaframework.boot.core.repository.domain.support.AuditorAwareStub;
import org.maculaframework.boot.core.repository.domain.support.DbDateTimeProvider;
import org.maculaframework.boot.core.repository.jpa.templatequery.template.FreemarkerSqlTemplates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;

/**
 * <p>
 * <b>JpaRepositoriesConfiguration</b> JPA Repository配置
 * </p>
 *
 * @author Rain
 * @since 2019-02-18
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareStub", dateTimeProviderRef = "dbDateTimeProvider")
@EnableConfigurationProperties(HibernateProperties.class)
@ConditionalOnClass(JpaRepository.class)
@ConditionalOnProperty(prefix = MaculaConstants.CONFIG_JPA_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(MaculaJpaRepositoriesConfigurationRegistar.class)
class JpaRepositoriesConfiguration {

    // JPA Audit配置
    @Bean
    @ConditionalOnMissingBean
    public AuditorAwareStub auditorAwareStub() {
        return new AuditorAwareStub();
    }

    @Bean
    @ConditionalOnMissingBean
    public DbDateTimeProvider dbDateTimeProvider() {
        return new DbDateTimeProvider();
    }

    // JPA TemplateQuery配置
    @Bean
    @ConditionalOnMissingBean
    public FreemarkerSqlTemplates freemarkerSqlTemplates() {
        return new FreemarkerSqlTemplates();
    }

    // 为了禁止TransactionAutoConfiguration
    @Bean
    public AbstractTransactionManagementConfiguration transactionManagementConfiguration() {
        return new AbstractTransactionManagementConfiguration() {

        };
    }

}
