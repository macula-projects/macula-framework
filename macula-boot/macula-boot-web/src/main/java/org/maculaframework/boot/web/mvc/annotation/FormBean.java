/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.maculaframework.boot.web.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> <b>FormBean</b> 是用于Spring MVC的Controller中的方法参数的注解，标识与HTML页面中Form 的对应关系，该标注会自动绑定页面Form传递进来的参数 </p>
 * 
 * @since 2011-3-16
 * @author Rain
 * @author Wilson Luo
 * @version $Id: FormBean.java 3807 2012-11-21 07:31:51Z wilson $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Documented
public @interface FormBean {

	String value() default "";

	/** 是否校验表单重复提交 */
	boolean valid() default false;

	/** 校验表单Token参数 */
	String token() default "ftoken";

	/** 是否使用验证码校验 */
	boolean captcha() default false;

	/** 表单数据是HTML ESCAPE */
	boolean htmlEscape() default false;
	
	/** 表单数据是JavaScript ESCAPE */
	boolean javascriptEscape() default false;
	
	/** 表单数据是SQL ESCAPE */
	boolean sqlEscape() default false;
}
