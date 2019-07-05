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
package org.maculaframework.boot.web.security.web.access.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.maculaframework.boot.core.service.Refreshable;
import org.maculaframework.boot.web.security.access.MaculaSecurityConfigAttribute;
import org.maculaframework.boot.web.security.support.ActionType;
import org.maculaframework.boot.web.security.support.SecurityResourceService;
import org.maculaframework.boot.web.security.support.vo.ActionVo;
import org.maculaframework.boot.web.security.support.vo.RoleVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p> <b>ActionFilterSecurityMetadataSource</b> 是功能提供实现. </p>
 * 
 * @since 2011-1-6
 * @author Wilson Luo
 * @version $Id: ActionFilterSecurityMetadataSource.java 5856 2015-09-22 12:49:38Z wzp $
 */

@Slf4j
public class ActionFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean, Refreshable {
	
	private static final ConfigAttribute NO_PERMISSION_ROLE = new SecurityConfig("ROLE_PRIVATE_");

	@Autowired(required = false)
	private SecurityResourceService securityResourceService;

	private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) {
		FilterInvocation invocation = (FilterInvocation) object;
		HttpServletRequest request = invocation.getRequest();
		
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			RequestMatcher matcher = entry.getKey();
			if (matcher.matches(request)) {
				return entry.getValue();
			}
		}
		return Collections.singleton(NO_PERMISSION_ROLE);
	}

	@Override
	public void afterPropertiesSet() {
		refresh();
	}

	@Override
	public boolean refresh() {
		List<ActionVo> actions = securityResourceService.findActions(ActionType.HTTP);

		Map<RequestMatcher, Collection<ConfigAttribute>> tempRequestMap = new LinkedHashMap<>();
		for (ActionVo action : actions) {
			HttpMethod httpMethod = action.getHttpMethod();
			RequestMatcher matcher = new RegexRequestMatcher(action.getUri(), httpMethod == null ? "" : httpMethod.name());

			Collection<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
			for (RoleVo role : action.getRoleVoList()) {
				attrs.add(new MaculaSecurityConfigAttribute(role));
			}
			if (attrs.isEmpty()) {
				attrs.add(NO_PERMISSION_ROLE);
			}

			// TODO 资源和用户直接关联怎么办？
			tempRequestMap.put(matcher, attrs);
		}

		requestMap = tempRequestMap;

		log.info("Action resources from database was loaded!");
		return true;
	}
}
