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

package org.maculaframework.boot.web.filter;

import com.google.code.kaptcha.Constants;
import org.springframework.boot.devtools.restart.FailureHandler;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * <b>KaptchaAuthenticationFilter</b> 验证码拦截器
 * </p>
 *
 * @author Rain
 * @since 2019-07-07
 */
public class KaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String servletPath;

    public KaptchaAuthenticationFilter(String servletPath, AuthenticationFailureHandler failureHandler) {
        super(servletPath);
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if ("POST".equalsIgnoreCase(req.getMethod()) && servletPath.equals(req.getServletPath())) {
            String expect = (String) req.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (expect != null && !expect.equalsIgnoreCase(req.getParameter("kaptcha"))) {
                unsuccessfulAuthentication(req, res,
                        new InsufficientAuthenticationException(messages.getMessage("KaptchaAuthenticationFilter.input.error", "输入验证码错误")));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        return null;
    }

}
