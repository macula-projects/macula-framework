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

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;

import java.lang.annotation.Annotation;

/**
 * <p>
 * <b>MaculaAnnotationRepositoryConfigurationSource</b> 添加RepositoryConfig属性
 * </p>
 *
 * @author Rain
 * @since 2019-02-20
 */
public class MaculaAnnotationRepositoryConfigurationSource extends AnnotationRepositoryConfigurationSource {

    private final AbstractRepositoryConfigurationSourceSupport.RepositoryConfig repositoryConfig;

    /**
     * Creates a new {@link AnnotationRepositoryConfigurationSource} from the given {@link AnnotationMetadata} and
     * annotation.
     *
     * @param metadata       must not be {@literal null}.
     * @param annotation     must not be {@literal null}.
     * @param resourceLoader must not be {@literal null}.
     * @param environment    must not be {@literal null}.
     * @param registry       must not be {@literal null}.
     */
    public MaculaAnnotationRepositoryConfigurationSource(AnnotationMetadata metadata,
                                                         Class<? extends Annotation> annotation,
                                                         ResourceLoader resourceLoader,
                                                         Environment environment,
                                                         BeanDefinitionRegistry registry,
                                                         AbstractRepositoryConfigurationSourceSupport.RepositoryConfig repositoryConfig) {

        super(metadata, annotation, resourceLoader, environment, registry);

        this.repositoryConfig = repositoryConfig;
    }

    public AbstractRepositoryConfigurationSourceSupport.RepositoryConfig getRepositoryConfig() {
        return this.repositoryConfig;
    }
}
