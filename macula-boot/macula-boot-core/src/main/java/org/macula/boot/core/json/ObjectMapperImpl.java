/**
 * Copyright 2010-2012 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.macula.boot.core.config.CoreConfigProperties;
import org.macula.boot.core.utils.XssCleaner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * <p> <b>ObjectMapperImpl</b> JSON 与 Java转换类 </p>
 *
 * @author zhengping_wang
 * @version $Id: ObjectMapperImpl.java 5136 2014-06-04 03:46:11Z wilson $
 * @since 2011-7-15
 */
public class ObjectMapperImpl extends com.fasterxml.jackson.databind.ObjectMapper {

    private static final long serialVersionUID = 1L;

    /**
     * 设置一些默认配置
     */
    public ObjectMapperImpl() {
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
