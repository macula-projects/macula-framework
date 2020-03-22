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

import org.maculaframework.boot.web.mvc.i18n.TimeZoneHeaderInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Rain
 * @since 2019-2-23
 */
public class MaculaWebMvcConfigurer implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TimeZoneHeaderInterceptor interceptor = new TimeZoneHeaderInterceptor();
        InterceptorRegistration i = registry.addInterceptor(interceptor);
        i.excludePathPatterns("*.gif|*.jpg|*.js|*.png");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new BufferedImageHttpMessageConverter());

        Jackson2ObjectMapperBuilder builder = applicationContext.getBean(Jackson2ObjectMapperBuilder.class);

        if (builder != null) {
            // 设置Jackson的ObjectMapper
            for (HttpMessageConverter<?> converter : converters) {
                if (converter instanceof MappingJackson2XmlHttpMessageConverter) {
                    ((MappingJackson2XmlHttpMessageConverter) converter).setObjectMapper(builder.createXmlMapper(true).build());
                }
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                     ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(builder.build());
                }
            }
        }
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        MessageSource messageSource = applicationContext.getBean(MessageSource.class);
        validator.setValidationMessageSource(messageSource);
        return validator;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
