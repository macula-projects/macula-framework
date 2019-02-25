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
 * SecurityHasAccessMethod.java 2016年10月19日
 */
package org.macula.boot.web.mvc.view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * <b>GetAbsoluteUrlMethod</b>获取绝对访问路径
 * </p>
 *
 * @since 2016年10月19日
 * @author Rain
 * @version $Id$
 */
public class GetAbsoluteUrlMethod implements TemplateMethodModelEx {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		Assert.notNull(arguments, "should input two argument--url & method");
		Assert.isTrue(arguments.size() == 3, "should input three argments");

		SimpleScalar base = (SimpleScalar) arguments.get(0);
		SimpleScalar url = (SimpleScalar) arguments.get(1);
		SimpleScalar def = (SimpleScalar) arguments.get(2);

		String urlString = "";
		if (url != null) {
			urlString = url.getAsString();
		}

		if (StringUtils.isEmpty(urlString) && def != null) {
			urlString = def.getAsString();
		}

		// 已经是绝对路径
		if (urlString.indexOf("://") >= 0) {
			return urlString;
		}

		// 加上斜杠
		if (!urlString.startsWith("/")) {
			urlString = "/" + urlString;
		}

		String baseString = base.getAsString();
		if (urlString.startsWith(baseString)) {
			return urlString;
		}

		// 加上上下文路径
		return baseString + urlString;
	}
}
