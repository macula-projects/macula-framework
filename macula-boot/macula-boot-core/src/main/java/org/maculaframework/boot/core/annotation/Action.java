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

package org.maculaframework.boot.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <b>Action</b> 权限定义注释，Macula Base提供扫描该注解的功能，从而导入到MB_ACTION表中
 * </p>
 *
 * @author Rain
 * @since 2020-06-16
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    /**
     * 角色
     *
     * @return 角色数组
     */
    String[] roles() default {};

    /**
     * 登陆后即可拥有的权限
     *
     * @return 是否拥有此权限
     */
    boolean actionLogin() default false;

    /**
     * 公共权限
     *
     * @return 是否拥有此权限
     */
    boolean actionPublic() default false;

    /**
     * 接口类型
     *
     * @return 是否为内部接口
     */
    boolean actionWithin() default false;
}
