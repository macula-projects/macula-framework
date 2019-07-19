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

package org.maculaframework.boot.web.security.core.userdetails;

import org.maculaframework.boot.web.security.CustomLoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

/**
 * <p>
 * <b>JpaUserDetailsManager</b> UserDetailsService实现，查找自定义的接口
 * </p>
 *
 * @author Rain
 * @since 2019-07-18
 */
public class JpaUserDetailsManager implements UserDetailsService, UserDetailsPasswordService {

    @Autowired(required = false)
    private CustomLoginUserService customUserLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (customUserLoginRepository != null) {
            return customUserLoginRepository.loadUserByUsername(username);
        }

        // 演示使用的用户和密码
        return User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        if (customUserLoginRepository != null) {
            return customUserLoginRepository.updatePassword(user, newPassword);
        }
        return user;
    }
}
