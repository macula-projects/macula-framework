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

/**
 * TemplateQueryParameterBinder.java 2016年10月30日
 */
package org.macula.boot.core.repository.templatequery;

import org.hibernate.query.internal.QueryImpl;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.ParameterBinder;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * <b>TemplateQueryParameterBinder</b>
 * 参数绑定，只支持命名参数，参数值允许原子类型、Enum、List、Array、Bean或者Map，Bean或者Map使用Key或者属性名称
 * 与命名参数对照，原子类型参数必须加上@Param("xxx")注解。
 * </p>
 *
 * @author Rain
 * @version $Id$
 * @since 2016年10月30日
 */
public class TemplateQueryParameterBinder extends ParameterBinder {

    private JpaQueryMethod method;
    private Object[] values;

    /**
     * Creates a new {@link ParameterBinder}.
     *
     * @param parameters must not be {@literal null}.
     * @param values     must not be {@literal null}.
     */
    public TemplateQueryParameterBinder(JpaQueryMethod method, Object[] values) {
        super(method.getParameters(), values);
        this.method = method;
        this.values = values;
    }

    @Override
    public <T extends Query> T bind(T query) {
        Map<String, Object> params = QueryBuilder.transValuesToMap(method.getParameters(), values);
        if (!CollectionUtils.isEmpty(params)) {
            // 将参数值设置到查询中，根据名称
            setQueryParams(query, params);
        }
        return query;
    }

    // 设置Query的参数值（通过命名参数设置，不支持位置设置）
    private void setQueryParams(Query query, Map<String, ?> params) {
        Query targetQuery = AopTargetUtils.getTarget(query);
        if (targetQuery instanceof QueryImpl) {
            // 必须是HibernateQuery才支持
            org.hibernate.Query hibernateQuery = ((QueryImpl<?>) targetQuery).getHibernateQuery();

            String[] nps = hibernateQuery.getNamedParameters();
            if (nps != null) {
                for (String key : nps) {
                    Object arg = params.get(key);
                    if (arg == null) {
                        hibernateQuery.setParameter(key, null);
                    } else if (arg.getClass().isArray()) {
                        hibernateQuery.setParameterList(key, (Object[]) arg);
                    } else if (arg instanceof Collection) {
                        hibernateQuery.setParameterList(key, ((Collection<?>) arg));
                    } else if (arg.getClass().isEnum()) {
                        hibernateQuery.setParameter(key, ((Enum<?>) arg).ordinal());
                    } else {
                        hibernateQuery.setParameter(key, arg);
                    }
                }
            }
        }
    }
}
