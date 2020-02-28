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

package org.maculaframework.boot.security.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>
 * <b>DelegatingFilterInvocationSecurityMetadataSource</b> 组合SecurityMetadataSource
 * </p>
 *
 * @author Rain
 * @since 2019-07-08
 */

@Slf4j
public class DelegatingFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final List<FilterInvocationSecurityMetadataSource> filterInvocationSecurityMetadataSources;

    public DelegatingFilterInvocationSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource... filterInvocationSecurityMetadataSources) {
        Assert.notNull(filterInvocationSecurityMetadataSources,
                "FilterInvocationSecurityMetadataSources cannot be null");
        this.filterInvocationSecurityMetadataSources = Arrays.asList(filterInvocationSecurityMetadataSources);
    }

    // ~ Methods
    // ========================================================================================================

    public Collection<ConfigAttribute> getAttributes(Object object) {
        Collection<ConfigAttribute> attributes = null;
        for (FilterInvocationSecurityMetadataSource s : filterInvocationSecurityMetadataSources) {
            attributes = s.getAttributes(object);
            if (attributes != null && !attributes.isEmpty()) {
                break;
            }
        }

        return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> set = new HashSet<>();
        for (FilterInvocationSecurityMetadataSource s : filterInvocationSecurityMetadataSources) {
            Collection<ConfigAttribute> attrs = s.getAllConfigAttributes();
            if (attrs != null) {
                set.addAll(attrs);
            }
        }
        return set;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public List<FilterInvocationSecurityMetadataSource> getFilterInvocationSecurityMetadataSources() {
        return filterInvocationSecurityMetadataSources;
    }

}
