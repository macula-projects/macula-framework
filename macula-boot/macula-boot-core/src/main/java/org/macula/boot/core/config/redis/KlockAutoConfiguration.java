/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.macula.boot.core.config.redis;

import io.netty.channel.nio.NioEventLoopGroup;
import org.macula.boot.core.klock.config.KlockConfig;
import org.macula.boot.core.klock.core.BusinessKeyProvider;
import org.macula.boot.core.klock.core.KlockAspectHandler;
import org.macula.boot.core.klock.core.LockInfoProvider;
import org.macula.boot.core.klock.lock.LockFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 * Created by kl on 2017/12/29.
 * Content :klock自动装配
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(KlockConfig.class) //使KlockConfig配置生效
@Import({KlockAspectHandler.class})
public class KlockAutoConfiguration {

    @Autowired
    private KlockConfig klockConfig;

    @Bean(destroyMethod = "shutdown") //bean 销毁时不调用shutdown
    @ConditionalOnMissingBean //仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean，@ConditionOnMissingBean(name = "example")，这个表示如果name为“example”的bean存在，这该注解修饰的代码块不执行。
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if(klockConfig.getClusterServer()!=null){
            config.useClusterServers().setPassword(klockConfig.getPassword())
                    .addNodeAddress(klockConfig.getClusterServer().getNodeAddresses());
        }else {
            config.useSingleServer().setAddress(klockConfig.getAddress())
                    .setDatabase(klockConfig.getDatabase())
                    .setPassword(klockConfig.getPassword());
        }
        Codec codec=(Codec) ClassUtils.forName(klockConfig.getCodec(),ClassUtils.getDefaultClassLoader()).newInstance();
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
    public LockFactory lockFactory(){
        return new LockFactory();
    }
}
