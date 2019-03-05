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

package org.macula.boot.web.mvc.convert;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Set;
import java.util.TimeZone;

/**
 * <p>
 * <b>StringToDateTimeConverter</b> Java8 DateTime Converter
 * </p>
 *
 * @author Rain
 * @since 2019-02-25
 */
public class StringToDateTimeConverter implements ConditionalGenericConverter {

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return false;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        // TODO 根据当前请求的TimeZone处理时间日期
        TimeZone timeZone = LocaleContextHolder.getTimeZone();

        return null;
    }
}
