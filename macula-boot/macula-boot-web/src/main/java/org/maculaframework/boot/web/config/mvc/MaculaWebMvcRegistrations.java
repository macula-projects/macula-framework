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

package org.maculaframework.boot.web.config.mvc;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * <p>
 * <b>MaculaWebMvcRegistrations</b> WebMvc配置的扩展
 * </p>
 *
 * @author Rain
 * @since 2019-02-21
 */
public class MaculaWebMvcRegistrations implements WebMvcRegistrations, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * Return the custom {@link RequestMappingHandlerMapping} that should be used and
     * processed by the MVC configuration.
     * @return the custom {@link RequestMappingHandlerMapping} instance
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return null;
    }

    /**
     * Return the custom {@link RequestMappingHandlerAdapter} that should be used and
     * processed by the MVC configuration.
     * @return the custom {@link RequestMappingHandlerAdapter} instance
     */
    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new org.maculaframework.boot.web.mvc.method.annotation.RequestMappingHandlerAdapter();
    }

    /**
     * Return the custom {@link ExceptionHandlerExceptionResolver} that should be used and
     * processed by the MVC configuration.
     * @return the custom {@link ExceptionHandlerExceptionResolver} instance
     */
    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return new org.maculaframework.boot.web.mvc.method.annotation.ExceptionHandlerExceptionResolver();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
