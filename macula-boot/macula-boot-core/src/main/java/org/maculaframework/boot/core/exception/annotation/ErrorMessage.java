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

/**
 * ErrorMessage.java 2015年9月24日
 */
package org.maculaframework.boot.core.exception.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * <b>ErrorMessage</b> Service方法上面默认错误信息的注解
 * </p>
 *
 * @since 2015年9月24日
 * @author Rain
 * @version $Id: ErrorMessage.java 5874 2015-09-24 11:22:28Z wzp $
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ErrorMessage {
	String value() default "";
}
