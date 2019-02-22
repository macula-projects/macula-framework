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

package org.macula.boot.core.repository.jpa.config;

import org.macula.boot.core.config.jpa.EntityManagerFactoryBeanBuilder;
import org.macula.boot.core.config.jpa.JpaBaseConfiguration;
import org.macula.boot.core.repository.jpa.MaculaJpaRepositoryFactoryBean;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * <p>
 * <b>RepositoryConfig</b> Spring Data Jpa扩展测试
 * </p>
 *
 * @author Rain
 * @since 2019-01-28
 */

@TestConfiguration
@EnableJpaRepositories(
        basePackages = "org.macula.boot.core.repository.jpa.support",
        repositoryFactoryBeanClass = MaculaJpaRepositoryFactoryBean.class,
        entityManagerFactoryRef = "maculaEntityManagerFactory",
        transactionManagerRef = "maculaTransactionManager")
public class RepositoryConfig extends JpaBaseConfiguration {

    private DataSource dataSource;

    // 默认内嵌的数据库DataSource，可以自己定义
    protected RepositoryConfig(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Bean(name = "maculaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean maculaEntityManagerFactory() {
        return getEntityManagerFactoryBuilder()
                .dataSource(dataSource)
                .properties(getVendorProperties())
                .packages("org.macula.**.domain") //设置实体类所在位置
                .persistenceUnit("maculaPersistenceUnit")
                .build();
    }

    @Bean(name = "maculaTransactionManager")
    public PlatformTransactionManager maculaTransactionManager(@Qualifier("maculaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean(name = "maculaTxAdvice")
    public TransactionInterceptor maculaTxAdvice(@Qualifier("maculaTransactionManager") PlatformTransactionManager transactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        transactionInterceptor.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        return transactionInterceptor;
    }

    @Bean(name = "maculaTxAdvisor")
    public AspectJExpressionPointcutAdvisor pointcutAdvisor(@Qualifier("maculaTxAdvice") TransactionInterceptor txAdvice){
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setAdvice(txAdvice);
        pointcutAdvisor.setExpression("execution(* org.macula..*.*(..)) and @within(org.springframework.stereotype.Service)");
        return pointcutAdvisor;
    }
}
