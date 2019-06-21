package org.maculaframework.boot.core.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

/**
 * <p>
 * <b>ConverterConfigurer</b> 自定义转换器
 * </p>
 *
 * @author Rain
 * @since 2019-03-19
 */
public abstract class ConverterConfigurer<S, D> {

    public abstract Converter<S, D> converter();

    @SuppressWarnings("unchecked")
    void configureImpl(ModelMapper mapper) {
        mapper.addConverter(converter());
    }
}