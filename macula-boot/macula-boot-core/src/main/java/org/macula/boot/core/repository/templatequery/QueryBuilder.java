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

package org.macula.boot.core.repository.templatequery;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.macula.boot.core.repository.templatequery.transformer.BeanTransformerAdapter;
import org.macula.boot.core.repository.templatequery.transformer.SmartTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.repository.query.Parameter;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/11.
 */
public class QueryBuilder {

    private static final Pattern ORDERBY_PATTERN_1 = Pattern.compile("order\\s+by.+?\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    /**
     * 获取COUNT的SQL语句
     *
     * @param query
     * @return String
     */
    public static String toCountQuery(String query) {
        return ORDERBY_PATTERN_1.matcher("select count(*) from (" + query + ") ctmp").replaceAll(")");
    }

    /**
     * 设置Query的结果转换
     *
     * @param query Query
     * @param clazz 返回类型
     * @return Query
     */
    public static <C> Query setResultTransformer(Query query, Class<C> clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive() || String.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)) {
            return query.setResultTransformer(new SmartTransformer(clazz));
        } else {
            return query.setResultTransformer(new BeanTransformerAdapter<C>(clazz));
        }
    }

    /**
     * 将接口中定义的查询方法参数转换为Map，原子类型直接放入map，Bean或者Map转换为Map后放入<BR/>
     * 为了防止不同的bean或者map属性重名，最终的MAP的KEY是参数名称.属性名称
     *
     * @param parameters 参数定义
     * @param values     参数值
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> transValuesToMap(JpaParameters parameters, Object[] values) {
        // gen model
        Map<String, Object> params = new HashMap<String, Object>();
        for (int i = 0; i < parameters.getNumberOfParameters(); i++) {
            Object value = values[i];
            Parameter parameter = parameters.getParameter(i);
            if (value != null && parameter.isBindable()) {
                if (!QueryBuilder.isValidValue(value)) {
                    continue;
                }
                Class<?> clz = value.getClass();
                if (clz.isPrimitive() || clz.isEnum() || String.class.isAssignableFrom(clz) || Number.class.isAssignableFrom(clz) || Date.class.isAssignableFrom(clz)) {
                    // 原子类型
                    params.put(parameter.getName(), value);
                } else if (clz.isArray() || value instanceof Collection<?>) {
                    // 集合
                    params.put(parameter.getName(), value);
                } else {
                    // 如果方法中的参数为Map或者Bean类型，则当成Map放入参数的Map中
                    // 为了防止不同的bean或者map属性重名，最终的MAP的KEY是参数名称.属性名称
                    Map<String, Object> map;
                    if (value instanceof Map) {
                        map = (Map<String, Object>) value;
                    } else {
                        map = transBeanToMap(value);
                    }
                    for (String key : map.keySet()) {
                        Object mapValue = map.get(key);
                        if (isValidValue(mapValue)) {
                            params.put(parameter.getName() + "." + key, mapValue);
                        }
                    }
                }
            }
        }
        return params;
    }

    public static Map<String, Object> transValuesToDataModel(JpaParameters parameters, Object[] values) {
        // gen model
        Map<String, Object> dataModel = new HashMap<String, Object>();
        for (int i = 0; i < parameters.getNumberOfParameters(); i++) {
            Object value = values[i];
            Parameter parameter = parameters.getParameter(i);
            String name = parameter.getName();
            if (name != null) {
                dataModel.put(name, value);
            }
        }
        return dataModel;
    }

    /**
     * 将Bean转为Map
     */
    private static Map<String, Object> transBeanToMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> description = new HashMap<String, Object>();
            if (bean instanceof DynaBean) {
                DynaProperty[] descriptors = ((DynaBean) bean).getDynaClass().getDynaProperties();
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();
                    description.put(name, BeanUtils.getProperty(bean, name));
                }
            } else {
                PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();
                    if (PropertyUtils.getReadMethod(descriptors[i]) != null) {
                        description.put(name, PropertyUtils.getNestedProperty(bean, name));
                    }
                }
            }
            return description;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 判断是否合法的Value
     *
     * @param object
     * @return boolean
     */
    private static boolean isValidValue(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Number && ((Number) object).longValue() == 0) {
            return false;
        }
        return !(object instanceof Collection && CollectionUtils.isEmpty((Collection<?>) object));
    }
}
