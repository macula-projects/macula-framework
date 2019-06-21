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
 * NamedTemplateResolver.java 2017年11月17日
 */
package org.maculaframework.boot.core.repository.jpa.templatequery.template;

import org.springframework.core.io.Resource;

/**
 * <p>
 * <b>NamedTemplateResolver</b> TemplateQuery模板解析接口
 * </p>
 *
 * @since 2017年11月17日
 * @author Rain
 * @version $Id$
 */
public interface NamedTemplateResolver {
	
	/**
	 * 模板后缀
	 * @return String
	 */
	String getSuffix();

	/**
	 * 解析模板中的SQL并回调
	 * @param resource 模板资源
	 * @param callback 回调函数，对应一个SQL
	 * @throws Exception
	 */
	void doInTemplateResource(Resource resource, final NamedTemplateCallback callback) throws Exception;
}
