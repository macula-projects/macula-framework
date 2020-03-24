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

package org.maculaframework.boot.core.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.maculaframework.boot.core.config.core.CoreConfigProperties;
import org.maculaframework.boot.core.utils.DateFormatUtils;
import org.maculaframework.boot.core.utils.XssCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * <b>MaculaJackson2ObjectMapperBuilderCustomizer</b> 定制Web上的Jackson
 * </p>
 *
 * @author Rain
 * @since 2019-02-23
 */
public class MaculaJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    /** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Autowired
    private JacksonProperties jacksonProperties;

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {

        // 默认忽略未知属性
        if (jacksonProperties.getDeserialization().get(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) == null) {
            builder.failOnUnknownProperties(false);
        }

        // bean为NULL时默认不报错
        if (jacksonProperties.getSerialization().get(SerializationFeature.FAIL_ON_EMPTY_BEANS) == null) {
            builder.failOnEmptyBeans(false);
        }

        if (jacksonProperties.getDeserialization().get(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE) == null) {
            builder.featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        }

        if (jacksonProperties.getSerialization().get(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) == null) {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        // 防止跨站脚本攻击
        if (CoreConfigProperties.isEnableEscapeXss()) {
            builder.serializers(new JsonXssEscapeSerializer());
        }

        //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
        builder.serializerByType(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        builder.serializerByType(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        builder.serializerByType(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        builder.deserializerByType(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        builder.deserializerByType(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        builder.deserializerByType(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));


        //Date序列化和反序列化
        builder.serializerByType(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                String formattedDate = formatter.format(date);
                jsonGenerator.writeString(formattedDate);
            }
        });
        builder.deserializerByType(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                String date = jsonParser.getText();
                try {
                    return DateFormatUtils.parseAll(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getOrder() {
        return 100;
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