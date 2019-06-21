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
package org.maculaframework.boot.web.mvc.convert;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * <p> <b>NumberToBooleanConverter</b> 是BigDecimal转为Boolean的转换器. </p>
 * 
 * @since 2011-8-25
 * @author Wilson Luo
 * @version $Id: NumberToBooleanConverter.java 5892 2015-10-08 08:44:59Z wzp $
 */
public class NumberToBooleanConverter implements ConditionalGenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> types = new HashSet<ConvertiblePair>();
		types.add(new ConvertiblePair(Number.class, Boolean.class));
		return types;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return source != null && ((Number) source).intValue() != 0;
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return Number.class.isAssignableFrom(sourceType.getType()) && Boolean.class == targetType.getType();
	}

}
