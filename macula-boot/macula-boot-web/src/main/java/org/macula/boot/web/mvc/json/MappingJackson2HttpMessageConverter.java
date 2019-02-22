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
package org.macula.boot.web.mvc.json;

import org.macula.boot.core.json.ObjectMapperImpl;
import org.macula.boot.web.utils.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * <b>MappingJackson2XmlHttpMessageConverter</b> 使用FastXML转换Java与XML
 * </p>
 *
 * @author zhengping_wang
 * @version $Id: MappingJackson2HttpMessageConverter.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2012-7-11
 */
public class MappingJackson2HttpMessageConverter extends
        org.springframework.http.converter.json.MappingJackson2HttpMessageConverter {

    private final static Logger logger = LoggerFactory.getLogger(MappingJackson2HttpMessageConverter.class);

    public MappingJackson2HttpMessageConverter() {
        super();
        this.setObjectMapper(new ObjectMapperImpl());
    }

    /**
     * 重载writeInternal，将ClientAbortException列印出来
     */
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        try {
            super.writeInternal(object, outputMessage);
        } catch (HttpMessageNotWritableException ex) {
            if (ex.getCause() != null && ex.getCause().getCause() != null
                    && "org.apache.catalina.connector.ClientAbortException".equals(ex.getCause().getClass().getName())) {
                StringBuffer errorMessage = new StringBuffer(ex.getCause().getCause().getMessage());
                errorMessage.append(" : ");
                HttpServletRequest request = HttpRequestUtils.getRequest();
                if (request != null) {
                    errorMessage.append(request.getRequestURL());
                    errorMessage.append("?");
                    errorMessage.append(request.getQueryString());
                }
                logger.error("ClientAbortException:" + errorMessage.toString());
            } else {
                throw ex;
            }
        }
    }
}
