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

package org.maculaframework.boot.web.security.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * <p>
 * <b>Menu</b> 菜单VO
 * </p>
 *
 * @author Rain
 * @since 2019-07-04
 */

@Getter
@Setter
public class Menu {
    /** 编码 */
    String code;

    /** 菜单名称 */
    String name;

    /** 菜单类型：分组，普通菜单，动作 */
    MenuType menuType;

    /** 路径正则表达式,HttpMethod:UrlRegex */
    String urlRegex;

    /** 所需权限代码，配合@Prexxx @Postxxx hasAuthorities('xxx') */
    String authority;

    /** 该正则表达式对应的请求关联的角色 */
    List<Role> roleVoList;
}
