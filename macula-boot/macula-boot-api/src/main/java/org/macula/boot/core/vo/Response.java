/**
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.macula.boot.ApiApplicationContext;
import org.macula.boot.core.exception.FormBindException;
import org.macula.boot.exception.MaculaException;
import org.springframework.util.StringUtils;

/**
 * <p>
 * <b>Result</b> 是Controller REST Service调用结果.记录服务调用时的异常信息.
 * </p>
 * 
 * @since 2011-7-7
 * @author Wilson Luo
 * @version $Id: Response.java 5939 2015-11-05 11:37:05Z wzp $
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 是否成功标识 */
	private boolean success;

	/** HTTP响应码 */
	private String httpStatus;

	/** 系统级错误代码 */
	private String errorCode;
	/** 系统级错误信息 */
	private String errorMessage;

	/** 业务级错误代码 */
	private String exceptionCode;
	/** 业务级错误信息 */
	private String exceptionMessage;

	/** 异常详细信息 */
	private String exceptionStack;

	/** 服务端重定向信息 */
	private String redirection;

	/** 校验结果信息 */
	private List<FieldError> validateErrors;

	public Response() {
		this.success = true;
	}

	public Response(MaculaException exception) {
		this.success = false;
		this.errorCode = exception.getParentCode();
		this.exceptionCode = exception.getMessage();
		this.exceptionMessage = exception.getLocalizedMessage();

		// if (exception.getCause() != null) {
		// this.exceptionMessage += ":" +
		// exception.getCause().getLocalizedMessage();
		// }

		this.exceptionStack = exception.getFullStackMessage();

		if (exception instanceof FormBindException) {
			List<FieldError> fieldErrors = ((FormBindException) exception).getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				this.addValidateError(fieldError);
			}
		}
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return the httpStatus
	 */
	public String getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		if (!StringUtils.isEmpty(errorMessage)) {
			return ApiApplicationContext.getMessage(errorMessage);
		}
		if (!StringUtils.isEmpty(errorCode)) {
			return ApiApplicationContext.getMessage(errorCode);
		}
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the exceptionCode
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * @param exceptionCode the exceptionCode to set
	 */
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	/**
	 * @return the exceptionMessage
	 */
	public String getExceptionMessage() {
		if (!StringUtils.isEmpty(exceptionMessage)) {
			return ApiApplicationContext.getMessage(exceptionMessage);
		}
		if (!StringUtils.isEmpty(exceptionCode)) {
			return ApiApplicationContext.getMessage(exceptionCode);
		}
		return exceptionMessage;
	}

	/**
	 * @param exceptionMessage the exceptionMessage to set
	 */
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return the validateErrors
	 */
	public List<FieldError> getValidateErrors() {
		return validateErrors;
	}

	public final void addValidateError(FieldError fieldError) {
		if (this.validateErrors == null) {
			validateErrors = new ArrayList<FieldError>();
		}
		validateErrors.add(fieldError);
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the exceptionStack
	 */
	public String getExceptionStack() {
		return exceptionStack;
	}

	/**
	 * @param exceptionStack the exceptionStack to set
	 */
	public void setExceptionStack(String exceptionStack) {
		this.exceptionStack = exceptionStack;
	}

	public void appendExceptionStack(String exception) {
		if (exceptionStack == null) {
			exceptionStack = exception;
		} else {
			exceptionStack += '\n' + exception;
		}
	}

	/**
	 * @return the redirection
	 */
	public String getRedirection() {
		return redirection;
	}

	/**
	 * @param redirection the redirection to set
	 */
	public void setRedirection(String redirection) {
		this.redirection = redirection;
	}
}
