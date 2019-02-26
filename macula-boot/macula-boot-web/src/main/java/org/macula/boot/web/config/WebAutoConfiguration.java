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

package org.macula.boot.web.config;

import org.macula.boot.core.config.CoreAutoConfiguration;
import org.macula.boot.web.config.support.MaculaWebMvcConfigurer;
import org.macula.boot.web.config.support.MaculaWebMvcRegistrations;
import org.macula.boot.web.mvc.bind.ConfigurableWebBindingInitializer;
import org.macula.boot.web.mvc.convert.StringToDateConverter;
import org.macula.boot.web.mvc.convert.NumberToBooleanConverter;
import org.macula.boot.web.mvc.convert.StringToDateTimeConverter;
import org.macula.boot.web.mvc.i18n.TimeZoneController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;

@Configuration
@AutoConfigureAfter({CoreAutoConfiguration.class})
@AutoConfigureBefore({WebMvcAutoConfiguration.class})
public class WebAutoConfiguration {

    @Autowired
    private WebMvcProperties webMvcProperties;

    @Bean
    public MaculaWebMvcRegistrations maculaWebMvcRegistrations() {
        return new MaculaWebMvcRegistrations();
    }

    @Bean
    public MaculaWebMvcConfigurer maculaWebMvcConfigurer() {
        return new MaculaWebMvcConfigurer();
    }

    @Bean
    public StringToDateConverter dateConverter() {
        return new StringToDateConverter();
    }

    @Bean
    public StringToDateTimeConverter dateTimeConverter() {
        return new StringToDateTimeConverter();
    }

    @Bean
    public NumberToBooleanConverter numberToBooleanConverter() {
        return new NumberToBooleanConverter();
    }

    @Bean
    public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
        return new BeanNameUrlHandlerMapping();
    }

    @Bean(name = "/timezone")
    public TimeZoneController timeZoneController() {
        return new TimeZoneController();
    }

    @Bean
    LocaleResolver localeResolver () {
        return new SessionLocaleResolver();
    }

    @Bean
    public ConfigurableWebBindingInitializer configurableWebBindingInitializer(ConversionService conversionService,
                                                                               Validator validator) {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(conversionService);
        initializer.setValidator(validator);
        initializer.setAutoGrowCollectionLimit(1000);
        return initializer;
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(webMvcProperties.getDateFormat())) {
            webMvcProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        }
    }
}
