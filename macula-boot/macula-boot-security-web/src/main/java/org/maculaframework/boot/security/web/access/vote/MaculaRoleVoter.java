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

package org.maculaframework.boot.security.web.access.vote;

import org.maculaframework.boot.security.web.access.MaculaSecurityConfigAttribute;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>
 * <b>MaculaRoleVoter</b> 基于角色的投票器，支持反向角色。即拥有某个角色就没有相应的权限。支持反向角色
 * </p>
 *
 * @author Rain
 * @since 2019-07-01
 */
public class MaculaRoleVoter extends RoleVoter {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof MaculaSecurityConfigAttribute;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {

        if (authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                MaculaSecurityConfigAttribute attr = (MaculaSecurityConfigAttribute)attribute;

                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attr.getAttribute().equals(authority.getAuthority())) {
                        if (attr.isOpposite()) {
                            return ACCESS_DENIED;
                        }
                        result = ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }
}
