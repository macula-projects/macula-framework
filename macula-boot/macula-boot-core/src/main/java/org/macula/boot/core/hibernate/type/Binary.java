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
 * <p> <b>Binary</b> 是Domain定义时用来替换byte[]类型的，可以配合 {@link MongoDbBinaryType} 或者 {@link RelationDbBinaryType}使用，
 * 以达到切换BLOB字段内容存入MongoDB或者Database之目的 </p>
 * 
 * @since 2011-3-7
 * @author Rain
 * @author Wilson Luo
 * @version $Id: Binary.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class Binary extends Lob {

	private static final long serialVersionUID = 1L;

	private transient byte[] content;

	public Binary() {
	}

	public Binary(byte[] content) {
		this.content = content == null ? null : content.clone();
	}

	public byte[] getContent() {
		return content; // NOSONAR
	}

	public void setContent(byte[] content) { // NOSONAR
		// 数据量可能占用较大内存，不做克隆
		this.content = content; // NOSONAR
	}

}
