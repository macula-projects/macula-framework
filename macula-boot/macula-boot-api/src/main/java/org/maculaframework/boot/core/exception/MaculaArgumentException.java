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
package org.maculaframework.boot.core.exception;

/**
 * <p> <b>MaculaArgumentException</b> 是输入参数异常. </p>
 * 
 * @since 2011-7-13
 * @author Wilson Luo
 * @version $Id: MaculaArgumentException.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class MaculaArgumentException extends MaculaException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param ex
	 */
	public MaculaArgumentException(IllegalArgumentException ex) {
		super(ex.getMessage(), ex);
	}

	/**
	 * @param code
	 * @param args
	 */
	public MaculaArgumentException(String code, Object... args) {
		super(code, args);
	}

}
