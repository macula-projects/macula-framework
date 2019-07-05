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

package org.maculaframework.boot.core.cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工厂类
 * @author yuhao.wang3
 */
public class BeanFactory {
    private static Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * bean 容器
     */
    private static ConcurrentHashMap<Class<?>, Object> beanContainer = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> aClass) {
        return (T) beanContainer.computeIfAbsent(aClass, aClass1 -> {
            try {
                return aClass1.newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        });
    }
}
