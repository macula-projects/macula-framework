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
package org.macula.boot.core.hibernate.type;

/**
 * <p>
 * <b>Text</b> 是Domain定义时用来替换String类型(CLOB)的，可以配合
 * {@link MongoDbTextType}或者 {@link RelationDbTextType}使用，
 * 以达到切换CLOB字段内容存入MongoDB或者Database之目的
 * </p>
 *
 * @since 2011-3-7
 * @author Rain
 * @author Wilson Luo
 * @version $Id: Text.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class Text extends Lob {

	private static final long serialVersionUID = 1L;

	private transient String content;

	public Text() {
	}

	public Text(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
