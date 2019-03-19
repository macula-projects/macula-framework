package org.macula.boot.core.modelmapper;

import org.macula.boot.core.modelmapper.config.ModelMapperConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>
 * Annotation that can be used in combination with @RunWith(SpringRunner.class) in case model mapper
 * component are needed for testing.
 * </p>
 *
 * @author Idan Rozenfeld
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ModelMapperConfiguration.class)
@ComponentScan(useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {TypeMapConfigurer.class, ConverterConfigurer.class, ConfigurationConfigurer.class}))
public @interface WithModelMapper {

    /**
     * Base packages to scan for mapper's components.
     * <p>
     * Use {@link #basePackageClasses} for a type-safe alternative to
     * String-based package names.
     */
    @AliasFor(annotation = ComponentScan.class)
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages} for specifying the packages
     * to scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package
     * that serves no purpose other than being referenced by this attribute.
     */
    @AliasFor(annotation = ComponentScan.class)
    Class<?>[] basePackageClasses() default {};
}