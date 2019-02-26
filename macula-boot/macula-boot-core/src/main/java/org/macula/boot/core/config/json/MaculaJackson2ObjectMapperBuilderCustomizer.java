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

package org.macula.boot.core.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.macula.boot.core.config.core.CoreConfigProperties;
import org.macula.boot.core.utils.XssCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * <p>
 * <b>MaculaJackson2ObjectMapperBuilderCustomizer</b> 定制Web上的Jackson
 * </p>
 *
 * @author Rain
 * @since 2019-02-23
 */
public class MaculaJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

    @Autowired
    private JacksonProperties jacksonProperties;

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {

        // 设置默认日期格式ISO8601
        if (StringUtils.isEmpty(jacksonProperties.getDateFormat())) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            builder.dateFormat(df);
        }

        // 默认忽略未知属性
        if (jacksonProperties.getDeserialization().get(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) == null) {
            builder.failOnUnknownProperties(false);
        }

        // bean为NULL时默认不报错
        if (jacksonProperties.getSerialization().get(SerializationFeature.FAIL_ON_EMPTY_BEANS) == null) {
            builder.failOnEmptyBeans(false);
        }

        // 防止跨站脚本攻击
        if (CoreConfigProperties.isEnableEscapeXss()) {
            builder.serializers(new JsonXssEscapeSerializer());
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