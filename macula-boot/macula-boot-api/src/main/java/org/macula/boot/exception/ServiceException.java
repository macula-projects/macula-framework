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

/**
 * ServiceException.java 2015年9月24日
 */
package org.macula.boot.exception;

/**
 * <p>
 * <b>ServiceException</b> 服务层默认异常
 * </p>
 *
 * @since 2015年9月24日
 * @author Rain
 * @version $Id: ServiceException.java 5874 2015-09-24 11:22:28Z wzp $
 */
public class ServiceException extends MaculaException {

	private static final long serialVersionUID = 1L;

	private String code;


	/**
	 * 服务层异常
	 * @param code 异常代码
	 * @param message 异常信息
	 * @param ex 原异常
	 */
	public ServiceException(String code, String message, Throwable ex) {
		super(code + ":" + message, ex);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
