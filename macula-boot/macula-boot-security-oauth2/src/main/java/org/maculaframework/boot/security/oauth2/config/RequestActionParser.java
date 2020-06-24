/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.security.oauth2.config;

import org.maculaframework.boot.core.annotation.Action;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * <b>RequestActionParser</b> 请求权限解析
 * </p>
 *
 * @author Rain
 * @since 2020-06-24
 */
public class RequestActionParser {

    private final RequestMappingHandlerMapping handlerMapping;

    public RequestActionParser(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public boolean extractor(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);
            if (handlerExecutionChain != null) {
                HandlerMethod method = (HandlerMethod) handlerExecutionChain.getHandler();
                if (method != null) {
                    Action action = AnnotationUtils.getAnnotation(method.getMethod(), Action.class);
                    if (action.actionPublic()) {
                        return true;
                    }
                }
            }
        } catch (Exception exception) {
        }

        // 判断是否登录，默认已登录都可以访问
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            return true;
        }

        return false;
    }
}
