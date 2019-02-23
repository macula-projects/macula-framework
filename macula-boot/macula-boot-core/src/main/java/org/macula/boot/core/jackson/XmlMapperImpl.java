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
package org.macula.boot.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.macula.boot.core.config.core.CoreConfigProperties;
import org.macula.boot.core.utils.XssCleaner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * <p> <b>XmlMapperImpl</b> XML 与 Java转换类 </p>
 *
 * @author zhengping_wang
 * @version $Id: XmlMapperImpl.java 3963 2013-01-16 09:39:09Z wilson $
 * @since 2012-7-11
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
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        this.registerModule(new JaxbAnnotationModule());
        this.registerModule(new JodaModule());
        this.registerModule(new ParameterNamesModule());
        this.registerModule(new Jdk8Module());
        this.registerModule(new JavaTimeModule());  // new module, NOT JSR310Module

        //防止跨站脚本攻击
        if (CoreConfigProperties.isEnableEscapeXss()) {
            SimpleModule module = new SimpleModule("XSS Escape Serializer", new Version(1, 0, 0, null, "org.macula.boot", "macula-boot-core"));
            module.addSerializer(new JsonXssEscapeSerializer());
            this.registerModule(module);
        }
    }

    private static class JsonXssEscapeSerializer extends JsonSerializer<String> {

        @Override
        public Class<String> handledType() {
            return String.class;
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                String escapedValue = XssCleaner.clean(value, CoreConfigProperties.getEscapeXssLevel());
                gen.writeString(escapedValue);
            }
        }
    }
}
