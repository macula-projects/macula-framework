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
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TimeZone;

/**
 * <p>
 * <b>TimeZoneRedirectInterceptor</b> 客户端时区处理拦截器
 * </p>
 *
 * @author Rain
 * @since 2019-02-26
 */

@Slf4j
public class TimeZoneRedirectInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle (HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {

        TimeZone timeZone = RequestContextUtils.getTimeZone(request);

        if (timeZone == null && !DispatcherType.ERROR.equals(request.getDispatcherType())) {
            log.debug("Forwarding to js to get timezone offset");
            request.setAttribute("requestedUrl", request.getRequestURI());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/timezone");
            dispatcher.forward(request, response);
            return false;
        }

        return true;
    }
}
