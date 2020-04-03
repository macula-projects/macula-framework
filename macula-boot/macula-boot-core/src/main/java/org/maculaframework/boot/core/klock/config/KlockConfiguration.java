/*
 * Copyright 2004-2019 the original author or authors.
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

package org.maculaframework.boot.core.klock.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.maculaframework.boot.core.klock.core.BusinessKeyProvider;
import org.maculaframework.boot.core.klock.core.KlockAspectHandler;
import org.maculaframework.boot.core.klock.core.LockInfoProvider;
import org.maculaframework.boot.core.klock.lock.LockFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 *
 * @author kl
 * @date 2017/12/29
 * Content :klock自动装配
 */

@Configuration
@EnableConfigurationProperties(KlockProperties.class)
@Import({KlockAspectHandler.class})
public class KlockConfiguration {

    @Bean(name = "klockRedissonClient", destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = "klockRedissonClient")
    RedissonClient klockRedissonClient(KlockProperties klockProperties) throws Exception {
        Config config = new Config();
        if(klockProperties.getClusterServer()!=null){
            config.useClusterServers().setPassword(klockProperties.getPassword())
                    .addNodeAddress(klockProperties.getClusterServer().getNodeAddresses());
        }else {
            config.useSingleServer().setAddress(klockProperties.getAddress())
                    .setDatabase(klockProperties.getDatabase())
                    .setPassword(klockProperties.getPassword());
        }
        Codec codec=(Codec) ClassUtils.forName(klockProperties.getCodec(),ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider(){
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider(){
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory(@Qualifier("klockRedissonClient") RedissonClient redissonClient, LockInfoProvider lockInfoProvider){
        return new LockFactory(redissonClient, lockInfoProvider);
    }
}
