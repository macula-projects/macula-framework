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

package org.maculaframework.boot.security.web;

import org.maculaframework.boot.security.web.support.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * <p>
 * <b>SecurityUtils</b> 安全助手类，主要给外部使用
 * </p>
 *
 * @author Rain
 * @since 2019-07-03
 */
public abstract class SecurityUtils {

    /**
     * 获取当前
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public static void configureAuthentication(String... role) {
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user",
                role,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前登录用户信息
     * @return 返回继承自UserDetails的用户信息
     */
    public static User getCurrentUser() {
        Authentication auth = getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return (User)principal;
        }

        return null;
    }

    /**
     * 获取当前登录的用户名，匿名用户返回ANONYMOUS
     * @return 当前用户名
     */
    public static String getCurrentUsername() {
        Authentication auth = getAuthentication();
        if (auth != null) {
            return auth.getName();
        }
        return null;
    }
}
