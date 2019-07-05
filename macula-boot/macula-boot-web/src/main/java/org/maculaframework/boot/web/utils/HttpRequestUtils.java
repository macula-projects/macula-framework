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
package org.maculaframework.boot.web.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> <b>HttpRequestUtils</b> 是HttpRequest的操作助手类. </p>
 * 
 * @since 2011-5-25
 * @author Wilson Luo
 * @version $Id: HttpRequestUtils.java 4702 2013-11-28 09:32:13Z wilson $
 */
public final class HttpRequestUtils {

	private static final String AJAX_MARK_UP = "isAjaxRequest";

	public static final String AJAX_REQUEST_HEADER = "X-Requested-With";

	public static final String AJAX_REQUEST_VALUE = "XMLHttpRequest";

	public static final String API_REQUEST_VALUE = "OpenAPIRequest";

	private HttpRequestUtils() {
		// Noops
	}

	/**
	 * 判断HttpServletRequest是否是AJAX或者OpenAPI请求
	 * @param request
	 * @return Boolean
	 */
	public static boolean isAjaxOrOpenAPIRequest(HttpServletRequest request) {
		String requestType = request.getHeader(AJAX_REQUEST_HEADER);
		return AJAX_REQUEST_VALUE.equals(requestType) || API_REQUEST_VALUE.equals(requestType);
	}

	/**
	 * 判断你是否是AJAX请求
	 * @param request
	 * @return Boolean
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader(AJAX_REQUEST_HEADER);
		return AJAX_REQUEST_VALUE.equals(requestType);
	}

	/**
	 * 判断是否是OpenAPI请求
	 * @param request
	 * @return Boolean
	 */
	public static boolean isOpenAPIRequest(HttpServletRequest request) {
		String requestType = request.getHeader(AJAX_REQUEST_HEADER);
		return API_REQUEST_VALUE.equals(requestType);
	}

	/**
	 * 将当前请求标记为AJAX请求
	 * @param request
	 */
	public static void markAsAjaxRequest(HttpServletRequest request) {
		if (request.getAttribute(AJAX_MARK_UP) == null) {
			request.setAttribute(AJAX_MARK_UP, Boolean.TRUE);
		}
	}

	/**
	 * 判断是否被标记为AJAX请求
	 * @param request
	 * @return
	 */
	public static boolean isMarkAsAjaxRequest(HttpServletRequest request) {
		return Boolean.TRUE == request.getAttribute(AJAX_MARK_UP);
	}

	/**
	 * 获取当前请求的浏览器类型
	 * @param request
	 * @return 返回User-Agent内容
	 */
	public static String getRequestBrowser(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	/**
	 * 获取浏览器客户端地址
	 * @param request
	 * @return 客户端地址
	 */
	public static String getRequestAddress(HttpServletRequest request) {
		String[] headNames = new String[] { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP" };
		String ip = null;
		for (int i = 0; i < headNames.length; i++) {
			ip = request.getHeader(headNames[i]);
			if (!StringUtils.isEmpty(ip)) {
				break;
			}
		}
		return StringUtils.isEmpty(ip) ? request.getRemoteAddr() : ip;
	}

	/**
	 * 获取当前HTTP线程的请求对象
	 * @return HttpServletRequest或者Null
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (null != attrs) {
			return attrs.getRequest();
		}
		return null;
	}
}
