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
 * NamedTemplateCallback.java 2017年11月17日
 */
package org.maculaframework.boot.core.repository.jpa.templatequery.template;

/**
 * <p>
 * <b>NamedTemplateCallback</b> 模板处理回调
 * </p>
 *
 * @since 2017年11月17日
 * @author Rain
 * @version $Id$
 */
public interface NamedTemplateCallback {
	/**
	 * 处理模板
	 * @param methodName 模板中的SQL名称
	 * @param content SQL语句模板
	 */
	void process(String methodName, String content);
}
