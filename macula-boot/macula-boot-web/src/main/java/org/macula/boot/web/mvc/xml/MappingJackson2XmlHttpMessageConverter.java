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
package org.macula.boot.web.mvc.xml;

import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * <p>
 * <b>MappingJackson2XmlHttpMessageConverter</b> 使用FastXML转换Java与XML
 * </p>
 *
 * @since 2012-7-11
 * @author zhengping_wang
 * @version $Id: MappingJackson2XmlHttpMessageConverter.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class MappingJackson2XmlHttpMessageConverter extends MappingJackson2HttpMessageConverter {

	public MappingJackson2XmlHttpMessageConverter() {
		super();
		MediaType supportedMediaType = new MediaType("application", "xml", DEFAULT_CHARSET);
		this.setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
		this.setObjectMapper(new XmlMapperImpl());
	}
}
