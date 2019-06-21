package org.maculaframework.boot.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

/**
 * <p>
 * <b>ConfigurationConfigurer</b> 全局ModelMapper配置
 * </p>
 *
 * @author Rain
 * @since 2019-03-19
 */
public abstract class ConfigurationConfigurer {

    void configureImpl(ModelMapper mapper) {
        configure(mapper.getConfiguration());
    }

    public abstract void configure(Configuration configuration);
}