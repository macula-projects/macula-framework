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

import lombok.Data;

/**
 * <p> <b>SimpleResult</b> 是简单数据类型返回结果. </p>
 * 
 * @since 2011-7-7
 * @author Wilson Luo
 * @version $Id: ExecuteResponse.java 5735 2015-08-17 08:31:52Z wzp $
 */

@Data
public class ExecuteResponse<T> extends Response {

	private static final long serialVersionUID = 1L;
	
	/** 结果信息 */
	private T returnObject;
	
	public ExecuteResponse(T result) {
		this.returnObject = result;
	}
}
