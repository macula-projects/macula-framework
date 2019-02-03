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

package org.macula.boot.core.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.macula.boot.MaculaConstants;
import org.macula.boot.core.utils.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>
 * <b>DataSourceBeanDefinitionRegistrar</b>
 * </p>
 *
 * @author Rain
 * @since 2019-02-02
 */

@Slf4j
public class DataSourceBeanDefinitionRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private Environment env;

    private Binder binder;

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            List<Map> list = binder.bind(MaculaConstants.CONFIG_DATASOURCE_PREFIX, Bindable.listOf(Map.class)).get();

            for (Map dsPropMap : list) {
                String dataSourceName = "dataSource-" + dsPropMap.get("name");
                if (!beanDefinitionRegistry.containsBeanDefinition(dataSourceName)) {
                    // 定义DataSourceBean
                    BeanDefinition dsBeanDef = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSourceWrapper.class)
                            .setInitMethodName("init")
                            .setDestroyMethodName("close")
                            .getBeanDefinition();
                    MutablePropertyValues mpv = dsBeanDef.getPropertyValues();

                    for (Object key : dsPropMap.keySet()) {
                        mpv.addPropertyValue(StringUtils.camelCaseName(key.toString()), dsPropMap.get(key));
                    }
                    beanDefinitionRegistry.registerBeanDefinition(dataSourceName, dsBeanDef);
                }
            }
        } catch (NoSuchElementException ex) {
            log.warn("No DataSource Configuration!!");
        }
    }

    /**
     * EnvironmentAware接口的实现方法，通过aware的方式注入，此处是environment对象
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        log.info("开始注册数据源");
        this.env = environment;
        // 绑定配置器
        binder = Binder.get(env);
    }
}
