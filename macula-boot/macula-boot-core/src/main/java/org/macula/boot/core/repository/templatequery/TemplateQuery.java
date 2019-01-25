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

import org.hibernate.jpa.internal.QueryImpl;
import org.macula.ApplicationContext;
import org.macula.boot.ApplicationContext;
import org.macula.boot.core.repository.TemplateQueryNotFoundException;
import org.macula.boot.core.repository.templatequery.template.FreemarkerSqlTemplates;
import org.macula.core.repository.TemplateQueryNotFoundException;
import org.macula.core.repository.templatequery.template.FreemarkerSqlTemplates;
import org.macula.core.utils.AopTargetUtils;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.ParameterBinder;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/9.
 */
public class TemplateQuery extends AbstractJpaQuery {

    /**
     * Creates a new {@link AbstractJpaQuery} from the given
     * {@link JpaQueryMethod}.
     *
     * @param method
     * @param em
     */
    public TemplateQuery(JpaQueryMethod method, EntityManager em) {
        super(method, em);
    }

    @Override
    protected Query doCreateQuery(Object[] values) {
        String queryString = getSqlFromTpl(values);

        ParameterAccessor accessor = new ParametersParameterAccessor(getQueryMethod().getParameters(), values);
        String sortedQueryString = QueryUtils.applySorting(queryString, accessor.getSort(), QueryUtils.detectAlias(queryString));

        Query nativeQuery = createJpaQuery(sortedQueryString);

        return createBinder(values).bindAndPrepare(nativeQuery);
    }

    @Override
    protected Query doCreateCountQuery(Object[] values) {
        String queryString = QueryBuilder.toCountQuery(getSqlFromTpl(values));

        Query nativeQuery = getEntityManager().createNativeQuery(queryString);

        return createBinder(values).bind(nativeQuery);
    }

    @Override
    protected ParameterBinder createBinder(Object[] values) {
        return new TemplateQueryParameterBinder(getQueryMethod(), values);
    }

    private String getSqlFromTpl(Object[] values) {
        Map<String, Object> dataModel = QueryBuilder.transValuesToDataModel(getQueryMethod().getParameters(), values);
        FreemarkerSqlTemplates sqlT = ApplicationContext.getBean(FreemarkerSqlTemplates.class);
        if (sqlT != null) {
            return sqlT.process(getQueryMethod(), dataModel);
        }
        throw new TemplateQueryNotFoundException("Can not find SqlTemplate Bean");
    }

    private Query createJpaQuery(String queryString) {
        Class<?> objectType = getQueryMethod().getReturnedObjectType();

        Query query = null;

        if (getQueryMethod().isQueryForEntity()) {
            // 返回类型是Domain
            query = getEntityManager().createNativeQuery(queryString, objectType);
        } else {
            // 非Domain返回类型，则找出最原始的返回类型
            query = getEntityManager().createNativeQuery(queryString);
            // find generic type
            ClassTypeInformation<?> ctif = ClassTypeInformation.from(objectType);
            TypeInformation<?> actualType = ctif.getActualType();
            Class<?> genericType = actualType.getType();

            if (genericType != null && genericType != Void.class) {
                // 必须是HibernateQuery才支持ResultTransformer
                Query targetQuery = AopTargetUtils.getTarget(query);
                if (targetQuery instanceof QueryImpl) {
                    // 设置查询结果到VO的Transformer
                    QueryBuilder.setResultTransformer(((QueryImpl<?>) targetQuery).getHibernateQuery(), genericType);
                }
            }
        }
        return query;
    }
}
