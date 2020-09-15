/*
 * Copyright 2004-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maculaframework.boot.core.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.maculaframework.boot.MaculaConstants;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.KryoCodec;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * <p>
 * <b>RedissonClientConfigurationRegistrar</b> Redisson动态配置的类
 * </p>
 *
 * @author Rain
 * @since 2020-04-02
 */

@Slf4j
public class RedissonClientConfigurationRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private Environment env;
    private Binder binder;

    @Override
    @SuppressWarnings("rawtypes")
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        log.info("开始注册Redisson");
        try {
            List<RedissonConfig> list = binder.bind(MaculaConstants.CONFIG_REDIS_PREFIX, Bindable.listOf(RedissonConfig.class)).get();
            for (RedissonConfig config : list) {
                // 默认使用KryoCodec
                if (config.getCodec() == null) {
                    config.setCodec(new KryoCodec());
                }
                RedissonClient redisson = Redisson.create(config);
                Object name = config.getName();
                if (name != null) {
                    String redissonClientBeanName = name + "RedissonClient";
                    if (!beanDefinitionRegistry.containsBeanDefinition(redissonClientBeanName)) {
                        // 注册 redisConnectionFactory
                        BeanDefinition redissonClientBeanDef = BeanDefinitionBuilder.genericBeanDefinition(RedissonClientFactoryBean.class)
                            .setDestroyMethodName("destroy")
                            .addConstructorArgValue(redisson)
                            .getBeanDefinition();

                        beanDefinitionRegistry.registerBeanDefinition(redissonClientBeanName, redissonClientBeanDef);
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("No Macula Redis Configuration");
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        // 绑定配置器
        binder = Binder.get(env);
    }
}
