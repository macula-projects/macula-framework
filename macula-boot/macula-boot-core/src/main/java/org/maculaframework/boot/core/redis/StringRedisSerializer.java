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

package org.maculaframework.boot.core.redis;

import org.maculaframework.boot.core.utils.JSONUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * 必须重写序列化器，否则@Cacheable注解的key会报类型转换错误
 *
 * @author yuhao.wang
 */
public class StringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    private final String target = "\"";

    private final String replacement = "";

    public StringRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(Object object) {
        String string = JSONUtils.objectToJson(object);
        if (string == null) {
            return null;
        }
        string = string.replace(target, replacement);
        return string.getBytes(charset);
    }
}
