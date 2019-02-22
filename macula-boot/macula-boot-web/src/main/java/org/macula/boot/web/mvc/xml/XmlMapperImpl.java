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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * <p> <b>XmlMapperImpl</b> XML 与 Java转换类 </p>
 * 
 * @since 2012-7-11
 * @author zhengping_wang
 * @version $Id: XmlMapperImpl.java 3963 2013-01-16 09:39:09Z wilson $
 */
public class XmlMapperImpl extends com.fasterxml.jackson.dataformat.xml.XmlMapper {

	private static final long serialVersionUID = 1L;

	/*
	/**********************************************************
	/* Life-cycle: construction, configuration
	/**********************************************************
	*/
	public XmlMapperImpl() {
		this(new XmlFactory());
	}

	public XmlMapperImpl(XmlFactory xmlFactory) {
		super(xmlFactory);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.setDateFormat(df);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		this.registerModule(new JaxbAnnotationModule());
		this.registerModule(new JodaModule());
	}
}
