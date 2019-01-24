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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> <b>FieldOption</b> 是可选值. </p>
 * 
 * @since 2011-5-18
 * @author Wilson Luo
 * @version $Id: FieldOption.java 5926 2015-11-03 07:37:42Z arron.lin $
 */

@Data
public class FieldOption {

	public static final String CODE = "code";
	public static final String LABEL = "label";
	public static final String ID = "id"; //Arron20151028

	private Object code;
	private String label;
	private BigDecimal id; //Arron20151028
	private final Map<String, Object> extra = new HashMap<String, Object>();

	public FieldOption(Object code, String label) {
		this.code = code;
		this.label = label;
	}
	
	public FieldOption(Object code, String label, BigDecimal id) {
		this.code = code;
		this.label = label;
		this.id = id;
	}
}
