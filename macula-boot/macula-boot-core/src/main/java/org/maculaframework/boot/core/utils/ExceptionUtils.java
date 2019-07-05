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
package org.maculaframework.boot.core.utils;

/**
 * <p> <b>ExceptionUtils</b> 异常助手 </p>
 * 
 * @since 2011-10-10
 * @author Wilson Luo
 * @version $Id: ExceptionUtils.java 3807 2012-11-21 07:31:51Z wilson $
 */
public final class ExceptionUtils {

	private ExceptionUtils() {
		//Noops
	}

	@SuppressWarnings("unchecked")
	public static <T> T getRecursionCauseException(Throwable e, Class<T> clzz) {
		if (e == null) {
			return null;
		}

		if (clzz.isAssignableFrom(e.getClass())) {
			return (T) e;
		}
		return getRecursionCauseException(e.getCause(), clzz);
	}
}
