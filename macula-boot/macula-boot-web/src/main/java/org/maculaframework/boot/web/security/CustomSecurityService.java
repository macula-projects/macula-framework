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

package org.maculaframework.boot.web.security;

import org.maculaframework.boot.web.security.support.Menu;
import org.maculaframework.boot.web.security.support.Role;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * <b>CustomSecurityService</b> 安全服务接口
 * </p>
 *
 * @author Rain
 * @since 2019-11-03
 */
public interface CustomSecurityService {
    /**
     * 查询菜单定义中包含URL正则的内容，主要给权限系统匹配每个URL对应的角色
     * @param appName 应用名称
     * @return
     */
    List<Menu> findUrlRegexes(String appName);

    /**
     * 获取菜单资源，不含ACTION，主要给前端显示菜单
     * @param root 根ID
     * @param level 层数
     * @return 满足条件的菜单信息
     */
    List<Menu> findMenus(String appName, int root, int level);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 更新用户密码
     * @param user
     * @param newPassword
     * @return 更新后的用户信息
     */
    UserDetails updatePassword(UserDetails user, String newPassword);
}
