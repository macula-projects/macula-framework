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
package org.maculaframework.boot.web.mvc.annotation;

import org.maculaframework.boot.vo.ExecuteResponse;
import org.maculaframework.boot.vo.PageResponse;

import java.lang.annotation.*;

/**
 * <p> <b>OpenApi</b> 标识是一个开放API的注解，标注该注解的方法的返回值会根据需要自动 包裹为 {@link ExecuteResponse}或者{@link PageResponse} </p>
 *
 * @author Rain Wang
 * @version $Id: OpenApi.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-7-8
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OpenApi {

}
