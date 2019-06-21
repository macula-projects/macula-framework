package org.maculaframework.boot.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * A factory bean that instantiates the {@link ModelMapper} and configures it by delegating to registered
 * {@link TypeMapConfigurer} instances.
 *
 * @author Idan Rozenfeld
 */
public class ModelMapperFactoryBean implements FactoryBean<ModelMapper> {

    /**
     * The global configuration used for customizing the behaviour of the model mapper.
     */
    @Autowired(required = false)
    private ConfigurationConfigurer mapperConfigurer;

    /**
     * The list of configurers used for customizing the behaviour of the model mapper.
     */
    @Autowired(required = false)
    private List<TypeMapConfigurer<?, ?>> configurers;

    /**
     * The list of converters used for customizing the behaviour of the model mapper.
     */
    @Autowired(required = false)
    private List<ConverterConfigurer<?, ?>> converters;

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelMapper getObject() throws Exception {
        final ModelMapper modelMapper = new ModelMapper();
        configure(modelMapper);
        return modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return ModelMapper.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Configures the model mapper instance be delegating toward registered {@link TypeMapConfigurer} instances.
     *
     * @param modelMapper the model mapper
     */
    private void configure(ModelMapper modelMapper) {
        if (mapperConfigurer != null) {
            mapperConfigurer.configureImpl(modelMapper);
        }

        if (converters != null) {
            converters.forEach(typeMapConfigurer -> typeMapConfigurer.configureImpl(modelMapper));
        }

        if (configurers != null) {
            configurers.forEach(typeMapConfigurer -> typeMapConfigurer.configureImpl(modelMapper));
        }
    }
}