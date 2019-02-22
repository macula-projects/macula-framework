package org.macula.boot.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.macula.boot.exception.ConvertException;
import org.macula.boot.core.json.ObjectMapperImpl;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * <b>JSONUtils</b> JSON序列号和反序列化助手类
 * </p>
 *
 * @author Rain
 * @since 2019-01-23
 */
@Slf4j
public class JSONUtils {
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapperImpl();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            throw new ConvertException(e);
        }
    }

    /**
     * 将JSON结果转换为对象，支持POJO或者List
     * @param jsonData json数据
     * @param type 对象的类型
     * @param <T> 返回类型
     * @return
     */
    public static <T> T jsonToObject(String jsonData, TypeReference<T> type) {
        try {
            T t = MAPPER.readValue(jsonData, type);
            return t;
        } catch (IOException e) {
            throw new ConvertException(e);
        }
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (IOException e) {
            throw new ConvertException(e);
        }
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }
}
