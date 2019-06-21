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
 * UserVo.java 2014年8月27日
 */
package org.maculaframework.boot.core.repository.jpa.support.domain;

import lombok.Data;

/**
 * <p>
 * <b>UserVo</b> is
 * </p>
 *
 * @since 2014年8月27日
 * @author Rain
 * @version $Id: UserVo.java 5351 2014-08-27 09:19:53Z wzp $
 */

@Data
public class UserVo {
	
	private String firstName;
	
	private String lastName;

	public UserVo() {
		
	}
	
	public UserVo(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
