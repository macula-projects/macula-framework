/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.maculaframework.boot.web.mvc.bind;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

/**
 * <p> <b>ConfigurableWebBindingInitializer</b> 是扩展的ConfigurableWebBindingInitializer，增加了设置autoGrowCollectionLimit的处理。
 * </p>
 * 
 * @since 2011-12-26
 * @author Wilson Luo
 * @version $Id: ConfigurableWebBindingInitializer.java 4780 2013-12-26 03:58:07Z wilson $
 */
public class ConfigurableWebBindingInitializer extends
		org.springframework.web.bind.support.ConfigurableWebBindingInitializer {

	private int autoGrowCollectionLimit = DataBinder.DEFAULT_AUTO_GROW_COLLECTION_LIMIT;

	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		binder.setAutoGrowCollectionLimit(autoGrowCollectionLimit);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * @param autoGrowCollectionLimit
	 *            the autoGrowCollectionLimit to set
	 */
	public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
		this.autoGrowCollectionLimit = autoGrowCollectionLimit;
	}

}
