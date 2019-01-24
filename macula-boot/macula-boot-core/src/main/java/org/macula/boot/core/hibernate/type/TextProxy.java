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
 * <p> <b>TextProxy</b> 是Text的延迟加载代理. </p>
 * 
 * @since 2011-3-15
 * @author Wilson Luo
 * @version $Id: TextProxy.java 3807 2012-11-21 07:31:51Z wilson $
 */
public final class TextProxy extends Text {

	private static final long serialVersionUID = 1L;

	private final Text target;
	private final ContentProvider<String> contentProvider;
	private boolean loaded = false;

	public TextProxy(Text target, ContentProvider<String> contentProvider) {
		this.target = target;
		this.contentProvider = contentProvider;
	}

	@Override
	public String getContent() {
		if (!loaded) {
			target.setContent(contentProvider.getContent());
			loaded = true;
		}
		return target.getContent();
	}

	@Override
	public void setContent(String content) {
		target.setContent(content);
		loaded = true;
	}

	@Override
	public String getId() {
		return target.getId();
	}

	@Override
	public void setId(String id) {
		target.setId(id);
	}

	@Override
	public boolean equals(Object obj) {
		return target.equals(obj);
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}

	@Override
	public String toString() {
		return target.toString();
	}

}
