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

import org.maculaframework.boot.core.utils.DateFormatUtils;
import org.maculaframework.boot.web.mvc.i18n.TimeZoneChangeInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author Rain
 * @since 2019-2-23
 */
public class MaculaWebMvcConfigurer implements WebMvcConfigurer, ApplicationContextAware {

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private ApplicationContext applicationContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 设置客户端时区拦截器
        TimeZoneChangeInterceptor interceptor = new TimeZoneChangeInterceptor();
        InterceptorRegistration i = registry.addInterceptor(interceptor);
        i.excludePathPatterns("*.gif|*.jpg|*.js|*.png");

        // 设置登录后拦截器

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 设置java8 time和Date默认的日期格式
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        });
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            }
        });
        registry.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        });
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                    return DateFormatUtils.parseAll(source.trim());
                } catch (ParseException e) {
                }
                return null;
            }
        });
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
