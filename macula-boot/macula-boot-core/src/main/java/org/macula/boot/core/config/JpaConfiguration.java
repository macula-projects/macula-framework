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

package org.macula.boot.core.config;

import org.macula.boot.core.domain.support.AuditorAwareStub;
import org.macula.boot.core.domain.support.DbDateTimeProvider;
import org.macula.boot.core.repository.templatequery.template.FreemarkerSqlTemplates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * <b>JpaConfiguration</b> Spring Data JPA相关配置
 * </p>
 *
 * @author Rain
 * @since 2019-01-31
 */

@Configuration
public class JpaConfiguration {
    // JPA Audit配置
    @Bean
    public AuditorAwareStub auditorAwareStub() {
        return new AuditorAwareStub();
    }

    @Bean
    public DbDateTimeProvider dbDateTimeProvider() {
        return new DbDateTimeProvider();
    }

    // JPA TemplateQuery配置
    @Bean
    public FreemarkerSqlTemplates freemarkerSqlTemplates() {
        return new FreemarkerSqlTemplates();
    }
}
