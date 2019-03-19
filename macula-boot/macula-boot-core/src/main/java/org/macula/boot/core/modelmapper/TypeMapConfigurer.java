package org.macula.boot.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.internal.typetools.TypeResolver;

/**
 * <p>
 * <b>TypeMapConfigurer</b> 类型映射自定义配置
 * </p>
 *
 * @author Rain
 * @since 2019-03-19
 */
public abstract class TypeMapConfigurer<S, D> {

    public String getTypeMapName() {
        return null;
    }

    public Configuration getConfiguration() {
        return null;
    }

    public abstract void configure(TypeMap<S, D> typeMap);

    @SuppressWarnings("unchecked")
    void configureImpl(ModelMapper mapper) {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(TypeMapConfigurer.class, getClass());
        String typeMapName = getTypeMapName();
        Configuration configuration = getConfiguration();

        if (typeMapName == null && configuration == null) {
            configure(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1]));
        } else if (typeMapName == null) {
            configure(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], configuration));
        } else if (configuration == null) {
            configure(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], typeMapName));
        } else {
            configure(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], typeMapName, configuration));
        }
    }
}