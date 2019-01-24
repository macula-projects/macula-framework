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
 * <b>ContentProvider</b> 是内容延迟获取接口.
 * </p>
 *
 * @since 2011-3-15
 * @author Wilson Luo
 * @version $Id: ContentProvider.java 3807 2012-11-21 07:31:51Z wilson $
 */
public interface ContentProvider<T> {

	T getContent();
}
