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
package org.macula.boot.core.repository;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AnnotationRepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * MaculaJpaRepositoryFactory
 * </p>
 * 扩展自{@link JpaRepositoryFactory}
 *
 * @author Rain
 * @version $Id: MaculaJpaRepositoryFactory.java 5896 2015-10-14 03:25:10Z wzp $
 * @since 2011-2-15
 */
public class MaculaJpaRepositoryFactory extends JpaRepositoryFactory {

    private static final boolean QUERY_DSL_PRESENT = ClassUtils.isPresent("com.mysema.query.types.Predicate",
            MaculaJpaRepositoryFactory.class.getClassLoader());

    private static final Logger logger = LoggerFactory.getLogger(MaculaJpaRepositoryFactory.class);
    private final PersistenceProvider extractor;
    private EntityManager entityManager;
    private List<RepositoryProxyPostProcessor> postProcessors;

    /**
     * @param entityManager
     */
    public MaculaJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);

        // add advise for cat
        addRepositoryProxyPostProcessor(CatRepositoryProxyPostProcessor.INSTANCE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.support.RepositoryFactorySupport
     * #getRepository(java.lang.Class, java.lang.Object)
     */
    @Override
    public void addRepositoryProxyPostProcessor(RepositoryProxyPostProcessor processor) {
        super.addRepositoryProxyPostProcessor(processor);
        if (postProcessors == null) {
            postProcessors = new ArrayList<RepositoryProxyPostProcessor>();
        }
        postProcessors.add(processor);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.support.RepositoryFactorySupport
     * #getRepository(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {
        if (customImplementation instanceof JpaEntityManagerAware) {
            // 注入EntityManager
            ((JpaEntityManagerAware) customImplementation).setEntityManager(entityManager);

            // 创建自定义实现的代理，加上对Transactional的处理
            ProxyFactory result = new ProxyFactory();
            result.setTarget(customImplementation);

            RepositoryMetadata metadata = getMyRepositoryMetadata(repositoryInterface);
            Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
            RepositoryInformation information = getRepositoryInformation(metadata, customImplementationClass);

            // 排除系统自带的接口，添加自定义接口给代理
            for (Class<?> intf : repositoryInterface.getInterfaces()) {
                if (!Repository.class.isAssignableFrom(intf) && !JpaSpecificationExecutor.class.isAssignableFrom(intf)) {
                    result.addInterface(intf);
                }
            }

            for (RepositoryProxyPostProcessor processor : postProcessors) {
                processor.postProcess(result, information);
            }
            return super.getRepository(repositoryInterface, result.getProxy());
        }
        return super.getRepository(repositoryInterface, customImplementation);
    }

    /**
     * Returns the {@link RepositoryMetadata} for the given repository
     * interface.
     *
     * @param repositoryInterface
     */
    RepositoryMetadata getMyRepositoryMetadata(Class<?> repositoryInterface) {
        return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface)
                : new AnnotationRepositoryMetadata(repositoryInterface);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.jpa.repository.support.JpaRepositoryFactory#
     * getRepositoryBaseClass(java.lang.Class)
     */
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return MaculaQueryDslJpaRepository.class;
        } else {
            return MaculaSimpleJpaRepository.class;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.jpa.repository.support.JpaRepositoryFactory#
     * getTargetRepository(org.springframework.data.repository.support.
     * RepositoryMetadata, javax.persistence.EntityManager)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information,
                                                                                         EntityManager entityManager) {

        Class<?> repositoryInterface = information.getRepositoryInterface();
        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());

        if (isQueryDslExecutor(repositoryInterface)) {
            return new MaculaQueryDslJpaRepository(entityInformation, entityManager);
        } else {
            return new MaculaSimpleJpaRepository(entityInformation, entityManager);
        }
    }

    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
        return TemplateQueryLookupStrategy.create(entityManager, key, extractor, evaluationContextProvider);
    }

    /**
     * Returns whether the given repository interface requires a QueryDsl
     * specific implementation to be chosen.
     *
     * @param repositoryInterface
     */
    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }

    // for cat
    enum CatRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {

        INSTANCE;

        @Override
        public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
            factory.addAdvice(new CatMethodInterceptor(repositoryInformation));
        }

        class CatMethodInterceptor implements MethodInterceptor {

            private RepositoryInformation repositoryInformation;

            public CatMethodInterceptor(RepositoryInformation repositoryInformation) {
                this.repositoryInformation = repositoryInformation;
            }

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Object result = null;
                try {
                    // 获取当前repository的接口和方法名称，放入threadlocal中给cat使用，标识对应SQL的名称
                    String sqlName = repositoryInformation.getRepositoryInterface().getSimpleName() + "."
                            + invocation.getMethod().getName();

                    if (logger.isDebugEnabled()) {
                        logger.debug("====sql name is {}", sqlName);
                    }

                    RepositoryMethodNameHolder.set(sqlName);
                    result = invocation.proceed();
                } finally {
                    RepositoryMethodNameHolder.remove();
                }
                return result;
            }
        }
    }
    // end enum
}
