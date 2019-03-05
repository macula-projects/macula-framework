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

import com.alibaba.druid.pool.DruidDataSource;
import org.macula.boot.core.config.jdbc.DataSourceConfigurationRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * <b>DataSourceConfiguration</b> 数据源自动配置，支持多数据源自动注册bean
 * </p>
 *
 * @author Rain
 * @since 2019-02-02
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@Import({DataSourceConfigurationRegistrar.class})
class DataSourceConfiguration {

}
