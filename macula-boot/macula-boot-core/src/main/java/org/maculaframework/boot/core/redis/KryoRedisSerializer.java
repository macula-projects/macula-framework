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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.maculaframework.boot.core.cache.support.NullValue;
import org.maculaframework.boot.core.cache.support.SerializationException;
import org.maculaframework.boot.core.utils.JSONUtils;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.ByteArrayOutputStream;

/**
 * @param <T> T
 * @author yuhao.wang
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(Kryo::new);
    private Class<?>[] clazzs;

    public KryoRedisSerializer(Class<?>[] clazzs) {
        super();
        this.clazzs = clazzs;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }

        Kryo kryo = kryos.get();
        // 设置成false 序列化速度更快，但是遇到循环应用序列化器会报栈内存溢出
        kryo.setReferences(false);
        for (Class<?> clazz : clazzs) {
            kryo.register(clazz);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            kryo.writeClassAndObject(output, t);
            output.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(String.format("KryoRedisSerializer 序列化异常: %s, 【JSON：%s】", e.getMessage(), JSONUtils.objectToJson(t)), e);
        } finally {
            kryos.remove();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        Kryo kryo = kryos.get();
        // 设置成false 序列化速度更快，但是遇到循环应用序列化器会报栈内存溢出
        kryo.setReferences(false);
        for (Class<?> clazz : clazzs) {
            kryo.register(clazz);
        }

        try (Input input = new Input(bytes)) {

            Object result = kryo.readClassAndObject(input);
            if (result instanceof NullValue) {
                return null;
            }
            return (T) result;
        } catch (Exception e) {
            throw new SerializationException(String.format("KryoRedisSerializer 反序列化异常: %s, 【JSON：%s】", e.getMessage(), JSONUtils.objectToJson(bytes)), e);
        } finally {
            kryos.remove();
        }
    }
}