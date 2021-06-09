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
package org.maculaframework.boot.vo;

import lombok.Data;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * <b>Result</b> 是Controller REST Service调用结果.记录服务调用时的异常信息.
 * </p>
 * 
 * @since 2011-7-7
 * @author Wilson Luo
 * @version $Id: Response.java 5939 2015-11-05 11:37:05Z wzp $
 */

@Data
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 是否成功标识 */
	private boolean success;

	/** 系统级错误代码 */
	private String errCode;
	/** 系统级错误信息 */
	private String errDesc;

	/** 异常详细信息 */
	private String exceptionStack;

	/** 服务端重定向信息 */
	private String redirection;

	/** 校验结果信息 */
	private List<FieldError> validateErrors;

	public Response() {
		this.success = true;
	}

	/**
	 * 构造
	 * @param errCode 错误码
	 * @param errDesc 错误信息
	 */
	public Response(String errCode, String errDesc) {
		this.success = false;
		this.errCode = errCode;
		this.errDesc = errDesc;
	}

	/**
	 * 用异常构造返回
	 * @param exception 异常信息
	 */
	public Response(MaculaException exception) {
		this.success = false;
		if (exception instanceof ServiceException) {
			this.errCode = ((ServiceException)exception).getCode();
		}
		this.errDesc = exception.getLocalizedMessage();
		// this.exceptionStack = exception.getFullStackMessage();
	}
}
