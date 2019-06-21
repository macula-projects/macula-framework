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

package org.maculaframework.boot.core.config.repositories;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.util.Streamable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * <b>AbstractRepositoryConfigurationSourceSupport</b> Respository自动定义Bean的抽象类
 * </p>
 *
 * @author Rain
 * @since 2019-02-18
 */
@Slf4j
public abstract class AbstractRepositoryConfigurationSourceSupport implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware,
        EnvironmentAware {

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    private Environment environment;

    private Binder binder;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {

        log.info("开始注册Spring Data Repositories");

        List<RepositoryConfig> list = new ArrayList<>();

        try {
            list = binder.bind(getConfigPrefix(), Bindable.listOf(RepositoryConfig.class)).get();
        } catch (Exception ex) {
            log.warn("No Macula JPA Configuration");
        }

        for (RepositoryConfig repoCfg : list) {
            // 注册各持久化层相关的Bean(EntityManagerFactory, TransactionManager，TxAdvise，TxAdvisor)
            registerManagerBeans(registry, repoCfg);

            // 注册Spring Data Repositories相关
            new RepositoryConfigurationDelegate(getConfigurationSource(repoCfg, registry), this.resourceLoader, this.environment)
                    .registerRepositoriesIn(registry, getRepositoryConfigurationExtension());
        }
    }

    private AnnotationRepositoryConfigurationSource getConfigurationSource(RepositoryConfig repositoryConfig, BeanDefinitionRegistry beanDefinitionRegistry) {

        StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(getConfiguration(), true);

        return new MaculaAnnotationRepositoryConfigurationSource(metadata, getAnnotation(),
                this.resourceLoader, this.environment, beanDefinitionRegistry, repositoryConfig) {

            @Override
            public Optional<String> getRepositoryFactoryBeanClassName() {
                return Optional.of(AbstractRepositoryConfigurationSourceSupport.this
                        .getRepositoryFactoryBeanClassName().getName());
            }

            @Override
            public Streamable<String> getBasePackages() {
                return Streamable.of(getRepositoryConfig().getRepositoryPackages());
            }

            @Override
            public BootstrapMode getBootstrapMode() {
                return getRepositoryConfig().getBootstrapMode();
            }

            @Override
            public Optional<String> getAttribute(String name) {
                // 设置指定的entityManagerFactory和transactionManager，在JpaRepositoryConfigExtension中有使用
                if ("transactionManagerRef".equals(name)) {
                    return Optional.of("transactionManager-" + getRepositoryConfig().getName());
                }

                if ("entityManagerFactoryRef".equals(name)) {
                    return Optional.of("entityManagerFactory-" + getRepositoryConfig().getName());
                }

                return super.getAttribute(name);
            }
        };
    }

    /**
     * The Spring Data annotation used to enable the particular repository mvc.
     *
     * @return the annotation class
     */
    protected abstract Class<? extends Annotation> getAnnotation();

    /**
     * The configuration class that will be used by Spring Boot as a template.
     *
     * @return the configuration class
     */
    protected abstract Class<?> getConfiguration();

    /**
     * The {@link RepositoryConfigurationExtension} for the particular repository mvc.
     *
     * @return the repository configuration extension
     */
    protected abstract RepositoryConfigurationExtension getRepositoryConfigurationExtension();

    /**
     * 配置属性前缀
     * @return 配置属性前缀
     */
    protected abstract String getConfigPrefix();

    /**
     * @return 默认的FactoryBeanClass
     */
    protected abstract Class<?> getRepositoryFactoryBeanClassName();

    /**
     * 注册额外的相关Bean
     * @param registry
     * @param repositoryConfig
     */
    protected abstract void registerManagerBeans(BeanDefinitionRegistry registry, RepositoryConfig repositoryConfig);

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // 绑定配置器
        binder = Binder.get(environment);
    }

    @Data
    public static class RepositoryConfig {
        private String name;
        private String[] repositoryPackages;
        private String[] entityPackages;
        private String servicePackage;
        private BootstrapMode bootstrapMode = BootstrapMode.DEFAULT;
    }
}
