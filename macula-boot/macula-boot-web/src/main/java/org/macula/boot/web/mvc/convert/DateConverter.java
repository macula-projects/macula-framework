/**
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.web.mvc.convert;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.macula.boot.core.utils.DateFormatUtils;
import org.macula.boot.exception.ConvertException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * <p> <b>DateConverter</b> 是时间转换器. </p>
 * 
 * @since 2011-5-19
 * @author Wilson Luo
 * @version $Id: DateConverter.java 5906 2015-10-19 09:40:12Z wzp $
 */
public class DateConverter implements ConditionalGenericConverter {

	@Override	
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> types = new HashSet<ConvertiblePair>();
		types.add(new ConvertiblePair(String.class, Date.class));
		types.add(new ConvertiblePair(String.class, java.sql.Date.class));
		types.add(new ConvertiblePair(String.class, Timestamp.class));
		types.add(new ConvertiblePair(String.class, Time.class));
		return types;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String sourceValue = (String) source;
		if (StringUtils.isBlank(sourceValue)) {
			return null;
		}
		try {
			Date date = DateFormatUtils.parseAll(sourceValue);
			if (Timestamp.class == targetType.getType()) {
				return new Timestamp(date.getTime());
			} else if (java.sql.Date.class == targetType.getType()) {
				return new java.sql.Date(date.getTime());
			} else if (Time.class == targetType.getType()) {
				return new Time(date.getTime());
			}
			return date;
		} catch (ParseException e) {
			throw new ConvertException(e);
		}
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return String.class == sourceType.getType() && Date.class.isAssignableFrom(targetType.getType());
	}

}
