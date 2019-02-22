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
package org.macula.boot.exception;

/**
 * <p>
 * <b>HibernateDataAccessException</b> 所有HibernateException会被转为该异常
 * </p>
 *
 * @since 2011-10-28
 * @author zhengping_wang
 * @version $Id: HibernateDataAccessException.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class HibernateDataAccessException extends MaculaException {

	private static final long serialVersionUID = 1L;

	public HibernateDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public HibernateDataAccessException(String message, Object[] args) {
		super(message, args);
	}

	public HibernateDataAccessException(String message, Object[] args, Throwable cause) {
		super(message, args, cause);
	}

}
