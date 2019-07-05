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

package org.maculaframework.boot.core.config.jpa;

import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.config.repositories.AbstractRepositoryConfigurationSourceSupport;
import org.maculaframework.boot.core.repository.jpa.MaculaJpaRepositoryFactoryBean;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;

/**
 * <p>
 * <b>MaculaJpaRepositoriesConfigurationRegistar</b> Spring Data JPA配置注册
 * </p>
 *
 * @author Rain
 * @since 2019-02-18
 */
public class MaculaJpaRepositoriesConfigurationRegistar extends AbstractRepositoryConfigurationSourceSupport {

    private BeanFactory beanFactory;

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJpaRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableJpaRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @Override
    protected String getConfigPrefix() {
        return MaculaConstants.CONFIG_JPA_REPOSITORIES_PREFIX;
    }

    @Override
    protected Class<?> getRepositoryFactoryBeanClassName() {
        return MaculaJpaRepositoryFactoryBean.class;
    }

    @Override
    protected void registerManagerBeans(BeanDefinitionRegistry registry,
                                        AbstractRepositoryConfigurationSourceSupport.RepositoryConfig repositoryConfig) {

        Assert.notNull(repositoryConfig.getName(), "repository name not null");
        Assert.notNull(repositoryConfig.getEntityPackages(), "repository entity-packages not null");
        Assert.notNull(repositoryConfig.getRepositoryPackages(), "repository packages not null");
        Assert.notNull(repositoryConfig.getServicePackage(), "repository service package not null");

        String name = repositoryConfig.getName();
        String dataSourceBeanName = "dataSource-" + name;
        String entityManagerFactoryBeanName = "entityManagerFactory-" + name;
        String transactionManagerBeanName = "transactionManager-" + name;
        String txAdviceBeanName = "txAdvice-" + name;

        // 注册各持久化层相关的Bean(EntityManagerFactory, TransactionManager，TxAdvice，TxAdvisor)

        // 注册EntityManagerFactoryBeanBuilder
        DataSource dataSource = beanFactory.getBean(dataSourceBeanName, DataSource.class);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(EntityManagerFactoryBeanBuilder.class);
        builder.addConstructorArgValue(dataSource);
        builder.addConstructorArgValue(repositoryConfig);
        registry.registerBeanDefinition("entityManagerFactoryBeanBuilder-" + name, builder.getBeanDefinition());

        // 注册EntityManagerFactory
        builder = BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class);
        builder.setFactoryMethodOnBean("createEntityManagerFactory", "entityManagerFactoryBeanBuilder-" + name);
        registry.registerBeanDefinition(entityManagerFactoryBeanName, builder.getBeanDefinition());

        // 注册TransactionManager
        builder = BeanDefinitionBuilder.genericBeanDefinition(JpaTransactionManager.class);
        builder.addPropertyReference("entityManagerFactory", entityManagerFactoryBeanName);
        registry.registerBeanDefinition(transactionManagerBeanName, builder.getBeanDefinition());

        // 注册TxAdvice
        builder = BeanDefinitionBuilder.genericBeanDefinition(TransactionInterceptor.class);
        builder.addPropertyReference("transactionManager", transactionManagerBeanName);
        builder.addPropertyValue("transactionAttributeSource", new RootBeanDefinition(AnnotationTransactionAttributeSource.class.getName()));
        registry.registerBeanDefinition(txAdviceBeanName, builder.getBeanDefinition());

        // 注册TxAdvisor
        builder = BeanDefinitionBuilder.genericBeanDefinition(AspectJExpressionPointcutAdvisor.class);
        builder.addPropertyReference("advice", txAdviceBeanName);
        builder.addPropertyValue("expression", "execution(* " + repositoryConfig.getServicePackage() + "..*.*(..)) and @within(org.springframework.stereotype.Service)");
        registry.registerBeanDefinition("txAdvisor-" + name, builder.getBeanDefinition());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @EnableJpaRepositories
    private static class EnableJpaRepositoriesConfiguration {

    }
}
