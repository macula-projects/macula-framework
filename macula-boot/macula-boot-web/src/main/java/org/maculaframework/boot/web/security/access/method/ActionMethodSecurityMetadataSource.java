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
package org.maculaframework.boot.web.security.access.method;

import org.maculaframework.boot.core.service.Refreshable;
import org.maculaframework.boot.web.security.access.MaculaSecurityConfigAttribute;
import org.maculaframework.boot.web.security.support.ActionType;
import org.maculaframework.boot.web.security.support.SecurityResourceService;
import org.maculaframework.boot.web.security.support.vo.ActionVo;
import org.maculaframework.boot.web.security.support.vo.RoleVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p> <b>ActionMethodSecurityMetadataSource</b> 是方法资源类. </p>
 * 
 * @since 2011-1-9
 * @author Wilson Luo
 * @version $Id: ActionMethodSecurityMetadataSource.java 5852 2015-09-22 11:34:30Z wzp $
 */
public class ActionMethodSecurityMetadataSource extends AbstractMethodSecurityMetadataSource implements InitializingBean, Refreshable {

	private static final ConfigAttribute NO_PERMISSION_ROLE = new SecurityConfig("ROLE_PRIVATE_");

	private ConcurrentMap<String, Collection<ConfigAttribute>> cachedMethodMappings = new ConcurrentHashMap<String, Collection<ConfigAttribute>>();

	@Autowired(required = false)
	private SecurityResourceService securityResourceService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
		return cachedMethodMappings.get(generateMethodName(method, targetClass));
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	private String generateMethodName(Method method, Class<?> targetClass) {
		return targetClass.getName() + "." + method.getName();
	}

	@Override
	public boolean refresh() {
		ConcurrentMap<String, Collection<ConfigAttribute>> tmpCachedMethodMappings = new ConcurrentHashMap<String, Collection<ConfigAttribute>>();

		if (securityResourceService != null) {
			List<ActionVo> actions = securityResourceService.findActions(ActionType.METHOD);
			for (ActionVo action : actions) {
				Collection<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
				for (RoleVo role : action.getRoleVoList()) {
					attrs.add(new MaculaSecurityConfigAttribute(role));
				}
				if (attrs.isEmpty()) {
					attrs.add(NO_PERMISSION_ROLE);
				}

				if (!tmpCachedMethodMappings.containsKey(action.getUri())) {
					tmpCachedMethodMappings.put(action.getUri(), attrs);
				}
			}
			cachedMethodMappings = tmpCachedMethodMappings;
		}
		return true;
	}

	@Override
	public void afterPropertiesSet() {
		refresh();
	}
}
