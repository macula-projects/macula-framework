package org.macula.boot.core.modelmapper.config;

import org.macula.boot.core.modelmapper.ModelMapperFactoryBean;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * <p>
 * <b>ModelMapperConfiguration</b> ModelMapper总体配置
 * </p>
 *
 * @author Rain
 * @since 2019-03-19
 */


@Configuration
@ConditionalOnClass(ModelMapper.class)
public class ModelMapperConfiguration {

    @Bean
    @ConditionalOnMissingBean(ModelMapperFactoryBean.class)
    public ModelMapperFactoryBean modelMapperFactoryBean() {
        return new ModelMapperFactoryBean();
    }
}