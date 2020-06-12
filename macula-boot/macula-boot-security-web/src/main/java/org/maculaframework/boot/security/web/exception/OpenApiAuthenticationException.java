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
package org.maculaframework.boot.security.web.exception;

import org.maculaframework.boot.ApplicationContext;
import org.maculaframework.boot.core.exception.MaculaException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> <b>OpenApiAuthenticationException</b> OpenAPI认证异常 </p>
 * 
 * @since 2011-7-19
 * @author zhengping_wang
 * @version $Id: OpenApiParameterException.java 5582 2015-05-13 01:29:56Z wzp $
 */

public class OpenApiAuthenticationException extends MaculaException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public OpenApiAuthenticationException(HttpServletRequest request, HttpServletResponse response, String message) {
		super(message);
		setRequest(request, response);
	}

	public OpenApiAuthenticationException(HttpServletRequest request, HttpServletResponse response, String message, Throwable ex) {
		super(message, ex);
		setRequest(request, response);
	}

	private void setRequest(HttpServletRequest request, HttpServletResponse response) {
		// 由于出现异常后，系统并没有进入DispatchServlet，所以RequestContextHolder中并没有Request，所以需要自己设置
		// 以便getMessage时可以得到Locale
		RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = null;
		if (previousRequestAttributes == null
				|| previousRequestAttributes.getClass().equals(ServletRequestAttributes.class)) {
			requestAttributes = new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(requestAttributes, false);
		}

		// 处理请求中的locale参数
		String newLocale = request.getParameter(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
		if (newLocale != null) {
			LocaleResolver localeResolver = ApplicationContext.getBean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME,
					LocaleResolver.class);
			if (localeResolver != null) {
				request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
				localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
			}
		}
	}

}
