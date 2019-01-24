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
package org.macula.boot.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p> <b>MaculaException</b> MACULA框架的异常基类 </p>
 * 
 * @since 2011-1-8
 * @author Rain
 * @version $Id: MaculaException.java 5584 2015-05-18 07:54:35Z wzp $
 */
public abstract class MaculaException extends I18nException {

	private static final long serialVersionUID = 1L;

	public MaculaException(String message) {
		super(message);
	}

	public MaculaException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaculaException(String message, Object[] args) {
		super(message, args);
	}

	public MaculaException(String message, Object[] args, Throwable cause) {
		super(message, args, cause);
	}

	public String getFullStackMessage() {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        this.printStackTrace(pw);
        return sw.getBuffer().toString();
	}

}
