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
 * CustomFreeMarkerVIewModel.java 2014年9月23日
 */
package org.macula.boot.web.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;

/**
 * <p>
 * <b>CustomFreeMarkerVIewModel</b> 定制需要产生的全局的FreeMarker能够使用的数据
 * </p>
 *
 * @since 2014年9月23日
 * @author Rain
 * @version $Id: CustomFreeMarkerViewModel.java 5627 2015-06-15 01:16:13Z wzp $
 */
public interface CustomFreeMarkerViewModel extends Ordered {
	
	/**
	 * 把需要给所有FreeMarker View模板使用的变量放入model中，例如后台的菜单等<br/>
	 * 主要不要使用dateTimePattern,datePattern,timePattern,numberPattern,timeZone,<br/>
	 * timeZoneOffset,request,tokenValidation等系统变量名称
	 * @param model
	 * @param request
	 */
	public void exposeHelpers(Map<String, Object> model, HttpServletRequest request);
	
}
