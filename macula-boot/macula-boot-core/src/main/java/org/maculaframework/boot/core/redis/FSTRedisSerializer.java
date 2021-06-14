/*
 * Copyright 2004-2021 the original author or authors.
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

package org.maculaframework.boot.core.redis;

import io.netty.util.internal.ObjectUtil;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * <b>FSTRedisSerializer</b> FST Redis序列化
 * </p>
 *
 * @author Rain
 * @since 2021-06-14
 */
public class FSTRedisSerializer<T> implements RedisSerializer<T> {
    private FSTConfiguration fstConfiguration ;

    public FSTRedisSerializer() {
        fstConfiguration = FSTConfiguration.getDefaultConfiguration();
        fstConfiguration.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        return fstConfiguration.asByteArray(obj);
    }

    @Override
    public T deserialize(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        return (T)fstConfiguration.asObject(bytes);
    }
}
