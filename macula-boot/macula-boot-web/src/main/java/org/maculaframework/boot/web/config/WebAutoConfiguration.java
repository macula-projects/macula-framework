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

package org.maculaframework.boot.web.config;

import org.maculaframework.boot.core.config.CoreAutoConfiguration;
import org.maculaframework.boot.web.config.mvc.MaculaWebMvcConfigurer;
import org.maculaframework.boot.web.config.mvc.MaculaWebMvcRegistrations;
import org.maculaframework.boot.web.mvc.bind.ConfigurableWebBindingInitializer;
import org.maculaframework.boot.web.mvc.convert.NumberToBooleanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@AutoConfigureAfter({CoreAutoConfiguration.class})
@AutoConfigureBefore({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties(WebConfigProperties.class)
@ComponentScan(basePackages = "org.maculaframework.boot.web.controller")
public class WebAutoConfiguration {

    @Autowired
    private WebMvcProperties webMvcProperties;

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public MaculaWebMvcRegistrations maculaWebMvcRegistrations() {
        return new MaculaWebMvcRegistrations();
    }

    @Bean
    public MaculaWebMvcConfigurer maculaWebMvcConfigurer() {
        return new MaculaWebMvcConfigurer();
    }

    @Bean
    public NumberToBooleanConverter numberToBooleanConverter() {
        return new NumberToBooleanConverter();
    }

    @Bean
    LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public ConfigurableWebBindingInitializer configurableWebBindingInitializer(@Qualifier("mvcConversionService") ConversionService conversionService,
                                                                               Validator validator) {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(conversionService);
        initializer.setValidator(validator);
        initializer.setAutoGrowCollectionLimit(1000);
        return initializer;
    }
}
