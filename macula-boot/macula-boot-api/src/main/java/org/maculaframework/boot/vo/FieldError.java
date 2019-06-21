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
package org.maculaframework.boot.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * <b>FieldError</b> 是页面元素对应的错误
 * </p>
 *
 * @since 2011-3-2
 * @author Rain
 * @version $Id: FieldError.java 5734 2015-08-17 08:29:11Z wzp $
 */

@Data
public class FieldError implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 元素名，与页面元素名一致  
	private String element;
	
	// 错误信息  
	private String message;
	
	public FieldError(String element, String message) {
		this.element = element;
		this.message = message;
	}
}
