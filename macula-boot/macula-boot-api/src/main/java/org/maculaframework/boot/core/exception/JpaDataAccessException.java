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
package org.maculaframework.boot.core.exception;

/**
 * <p>
 * <b>JpaDataAccessException</b> 所有PersistenceException异常被转为该异常
 * </p>
 *
 * @since 2011-10-28
 * @author zhengping_wang
 * @version $Id: JpaDataAccessException.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class JpaDataAccessException extends MaculaException {

	private static final long serialVersionUID = 1L;

	public JpaDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public JpaDataAccessException(String message, Object[] args) {
		super(message, args);
	}

	public JpaDataAccessException(String message, Object[] args, Throwable cause) {
		super(message, args, cause);
	}
}
