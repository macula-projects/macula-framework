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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.QueryParameter;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.macula.boot.core.repository.templatequery.transformer.BeanTransformerAdapter;
import org.macula.boot.core.repository.templatequery.transformer.SmartTransformer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/11.
 */
public class QueryBuilder {

    private static final Pattern ORDERBY_PATTERN_1 = Pattern
            .compile("order\\s+by.+?$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    private static Map<Class<?>, ResultTransformer> transformerCache = new ConcurrentHashMap<>();

    public static void transform(NativeQuery<?> query, Class<?> clazz) {
        ResultTransformer transformer;
        if (Map.class.isAssignableFrom(clazz)) {
            transformer = Transformers.ALIAS_TO_ENTITY_MAP;
        } else if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive() || String.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz)) {
            transformer = transformerCache.computeIfAbsent(clazz, SmartTransformer::new);
        } else {
            transformer = transformerCache.computeIfAbsent(clazz, BeanTransformerAdapter::new);
        }
        query.setResultTransformer(transformer);
    }

    private static String wrapCountQuery(String query) {
        return "select count(*) from (" + query + ") as ctmp";
    }

    private static String cleanOrderBy(String query) {
        Matcher matcher = ORDERBY_PATTERN_1.matcher(query);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            String part = matcher.group(i);
            if (canClean(part)) {
                matcher.appendReplacement(sb, "");
            } else {
                matcher.appendReplacement(sb, part);
            }
            i++;
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static boolean canClean(String orderByPart) {
        return orderByPart != null && (!orderByPart.contains(")")
                ||
                StringUtils.countOccurrencesOf(orderByPart, ")") == StringUtils.countOccurrencesOf(orderByPart, "("));
    }

    public static String toCountQuery(String query) {
        return wrapCountQuery(cleanOrderBy(query));
    }

    public static void setParams(NativeQuery<?> query, Object beanOrMap) {

        Collection<QueryParameter> nps = query.getParameterMetadata().getNamedParameters();
        if (nps != null) {
            Map<String, Object> params = toParams(beanOrMap);
            for (QueryParameter<?> param : nps) {
                String key = param.getName();
                Object arg = params.get(key);
                if (arg == null) {
                    query.setParameter(key, null);
                } else if (arg.getClass().isArray()) {
                    query.setParameterList(key, (Object[]) arg);
                } else if (arg instanceof Collection) {
                    query.setParameterList(key, ((Collection) arg));
                } else if (arg.getClass().isEnum()) {
                    query.setParameter(key, ((Enum) arg).ordinal());
                } else {
                    query.setParameter(key, arg);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toParams(Object beanOrMap) {
        Map<String, Object> params;
        if (beanOrMap instanceof Map) {
            params = (Map<String, Object>) beanOrMap;
        } else {
            params = toMap(beanOrMap);
        }
        if (!CollectionUtils.isEmpty(params)) {
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!isValidValue(params.get(key))) {
                    keys.remove();
                }
            }
        }
        return params;
    }

    public static boolean isValidValue(Object object) {
        if (object == null) {
            return false;
        }
        /*if (object instanceof Number && ((Number) object).longValue() == 0) {
            return false;
		}*/
        return !(object instanceof Collection && CollectionUtils.isEmpty((Collection<?>) object));
    }

    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> description = new HashMap<String, Object>();
            if (bean instanceof DynaBean) {
                DynaProperty[] descriptors = ((DynaBean) bean).getDynaClass().getDynaProperties();
                for (DynaProperty descriptor : descriptors) {
                    String name = descriptor.getName();
                    description.put(name, BeanUtils.getProperty(bean, name));
                }
            } else {
                PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
                for (PropertyDescriptor descriptor : descriptors) {
                    String name = descriptor.getName();
                    if (PropertyUtils.getReadMethod(descriptor) != null) {
                        description.put(name, PropertyUtils.getNestedProperty(bean, name));
                    }
                }
            }
            return description;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }


    public static void main(String[] args) {
        String t1 = "select * from user order by id";
        String t2 = "select * from abc order by xxx(convert( resName using gbk )) collate gbk_chinese_ci asc";
        String t3 = "select count * from ((select * from aaa group by a order by a) union all (select * from aaa group by a order by a))";
        String t4 = "SELECT\n" +
                "  t1.*,t2.name AS dictionaryName,t3.name AS classifyName\n" +
                "FROM res_data_element t1 LEFT JOIN sys_business_dictionary t2 ON  t1.dictionary_id = t2.id\n" +
                "  LEFT JOIN sys_business_dictionary t3 ON  t1.classify = t3.id\n" +
                "WHERE  1=1\n" +
                "       AND  t1.is_history_version = 1\n" +
                "       AND t1.status = 1\n" +
                "       AND (t1.name LIKE '%${nameOrCodeOrENameOrCompany}%'\n" +
                "         OR\n" +
                "         t1.code LIKE '%${nameOrCodeOrENameOrCompany}%'\n" +
                "         OR\n" +
                "         t1.englishname LIKE '%${nameOrCodeOrENameOrCompany}%'\n" +
                "         OR\n" +
                "         t1.submit_company LIKE '%${nameOrCodeOrENameOrCompany}%')";
        System.out.println(QueryBuilder.toCountQuery(t1));
        System.out.println(QueryBuilder.toCountQuery(t2));
        System.out.println(QueryBuilder.toCountQuery(t3));
//        System.out.println(QueryBuilder.toCountQuery(t4));
    }
}
