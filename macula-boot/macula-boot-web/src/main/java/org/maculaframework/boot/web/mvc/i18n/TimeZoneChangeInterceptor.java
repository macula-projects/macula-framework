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

package org.maculaframework.boot.web.mvc.i18n;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.chrono.Chronology;
import java.util.TimeZone;

/**
 * <p>
 * <b>TimeZoneRedirectInterceptor</b> 客户端时区处理拦截器，没什么作用，先放着吧
 * </p>
 *
 * @author Rain
 * @since 2019-02-26
 */

@Slf4j
public class TimeZoneChangeInterceptor extends HandlerInterceptorAdapter {

    public static final String DEFAULT_PARAM_NAME = "timezone";
    private static final Logger logger = LoggerFactory.getLogger(TimeZoneChangeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ServletException {

        String newTimeZone = request.getHeader(DEFAULT_PARAM_NAME);
        if (StringUtils.isEmpty(newTimeZone)) {
            return true;
        }

        TimeZone timeZone = StringUtils.parseTimeZoneString(newTimeZone);
        if (timeZone != null) {
            logger.debug("CHANGE TIMEZONE:" + LocaleContextHolder.getTimeZone().getID() + " TO " + timeZone.getID());
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

            if (localeResolver instanceof LocaleContextResolver) {
                ((LocaleContextResolver) localeResolver).setLocaleContext(request, response,
                    new SimpleTimeZoneAwareLocaleContext(LocaleContextHolder.getLocale(), timeZone));
            }

            DateTimeContext dateTimeContext = new DateTimeContext();
            dateTimeContext.setTimeZone(timeZone.toZoneId());
            DateTimeContextHolder.setDateTimeContext(dateTimeContext);
        }

        return true;
    }
}