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
    private Class<T> clazz;

    public KryoRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }

        Kryo kryo = kryos.get();
        // 设置成false 序列化速度更快，但是遇到循环应用序列化器会报栈内存溢出
        kryo.setReferences(false);
        kryo.register(clazz);

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
        kryo.register(clazz);

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