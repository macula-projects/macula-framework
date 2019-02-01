package org.macula.boot.core.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * <p>
 * <b>CoreAutoConfiguration</b> Core模块的自动配置入口
 * </p>
 *
 * @author Rain
 * @since 2019-01-22
 */

@Configuration
@EnableConfigurationProperties({CoreConfigProperties.class})
@EnableRedisRepositories
@AutoConfigureBefore({RedisAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@Import({RedisConfiguration.class, JpaConfiguration.class})
public class CoreAutoConfiguration {

}
