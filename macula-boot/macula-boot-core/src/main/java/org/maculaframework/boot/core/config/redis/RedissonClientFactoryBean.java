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

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * <b>RedissonClientFactoryBean</b> RedissonClient的FactoryBean，用来包裹RedissonClient实例
 * </p>
 *
 * @author Rain
 * @since 2020-04-03
 */
public class RedissonClientFactoryBean implements FactoryBean<RedissonClient>, DisposableBean {

    private RedissonClient redissonClient;

    public RedissonClientFactoryBean(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void destroy() throws Exception {
        if (redissonClient != null) {
            redissonClient.shutdown();
        }
    }

    @Override
    public RedissonClient getObject() throws Exception {
        return redissonClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RedissonClient.class;
    }
}
