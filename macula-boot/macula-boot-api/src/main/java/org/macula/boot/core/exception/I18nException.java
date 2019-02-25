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
package org.macula.boot.core.exception;

import java.util.Locale;

import org.macula.boot.ApiApplicationContext;

/**
 * <p> <b>I18nException</b> 可以国际化消息的异常 </p>
 * 
 * @since 2011-1-8
 * @author Rain
 * @version $Id: I18nException.java 3807 2012-11-21 07:31:51Z wilson $
 */
public abstract class I18nException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object[] args = null;

	public I18nException() {
		super();
	}

	public I18nException(Throwable cause) {
		super(cause);
	}

	public I18nException(String message) {
		super(message);
	}

	public I18nException(String message, Throwable cause) {
		super(message, cause);
	}

	public I18nException(String message, Object[] args) {
		super(message);
		this.args = args == null ? null : args.clone();
	}

	public I18nException(String message, Object[] args, Throwable cause) {
		super(message, cause);
		this.args = args == null ? null : args.clone();
	}

	@Override
	public String getLocalizedMessage() {
		return ApiApplicationContext.getMessage(getMessage(), args);
	}

	public String getLocalizedMessage(Locale locale) {
		return ApiApplicationContext.getMessage(getMessage(), args, getMessage(), locale);
	}
}
