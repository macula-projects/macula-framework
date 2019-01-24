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

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>
 * <b>Lob</b> 是大字段类型.
 * </p>
 *
 * @since 2011-3-9
 * @author Wilson Luo
 * @version $Id: Lob.java 4947 2014-03-10 01:35:27Z wilson $
 */
public abstract class Lob implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		String hashObject = (getId() == null) ? StringUtils.EMPTY : getId();
		return HashCodeBuilder.reflectionHashCode(hashObject);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
