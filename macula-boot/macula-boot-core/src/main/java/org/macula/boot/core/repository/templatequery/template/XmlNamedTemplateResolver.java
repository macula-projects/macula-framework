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
 * XmlNamedTemplateResolver.java 2017年11月17日
 */
package org.macula.boot.core.repository.templatequery.template;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * <p>
 * <b>XmlNamedTemplateResolver</b> XML模板加载处理
 * </p>
 *
 * @since 2017年11月17日
 * @author Rain
 * @version $Id$
 */
public class XmlNamedTemplateResolver implements NamedTemplateResolver {
	protected final Log logger = LogFactory.getLog(getClass());

	private String encoding = "UTF-8";

	private DocumentLoader documentLoader = new DefaultDocumentLoader();

	private EntityResolver entityResolver;

	private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);

	public XmlNamedTemplateResolver(String encoding, ResourceLoader resourceLoader) {
		this.encoding = encoding;
		this.entityResolver = new ResourceEntityResolver(resourceLoader);
	}

	/**
	 * 模板后缀
	 *
	 * @return String
	 */
	@Override
	public String getSuffix() {
		return "xml";
	}

	@Override
	public Iterator<Void> doInTemplateResource(Resource resource, final NamedTemplateCallback callback)
			throws Exception {
		InputSource inputSource = new InputSource(resource.getInputStream());
		inputSource.setEncoding(encoding);
		Document doc = documentLoader.loadDocument(inputSource, entityResolver, errorHandler,
				XmlValidationModeDetector.VALIDATION_XSD, false);
		final List<Element> sqes = DomUtils.getChildElementsByTagName(doc.getDocumentElement(), "sql");

		return new Iterator<Void>() {
			int index = 0, total = sqes.size();

			@Override
			public boolean hasNext() {
				return index < total;
			}

			@Override
			public Void next() {
				Element sqle = sqes.get(index);
				callback.process(sqle.getAttribute("name"), sqle.getTextContent());
				index++;
				return null;
			}

			@Override
			public void remove() {
				//ignore
			}
		};
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}