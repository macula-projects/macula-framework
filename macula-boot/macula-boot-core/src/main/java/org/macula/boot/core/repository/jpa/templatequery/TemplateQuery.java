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

package org.macula.boot.core.repository.jpa.templatequery;

import org.hibernate.query.NativeQuery;
import org.macula.boot.ApplicationContext;
import org.macula.boot.core.repository.jpa.templatequery.template.FreemarkerSqlTemplates;
import org.macula.boot.core.utils.AopTargetUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/9.
 */
public class TemplateQuery extends AbstractJpaQuery {

    private boolean useJpaSpec = true;

    /**
     * Creates a new {@link AbstractJpaQuery} from the given {@link JpaQueryMethod}.
     *
     * @param method org.macula.boot.core.repository query method
     * @param em     entity manager
     */
    TemplateQuery(JpaQueryMethod method, EntityManager em) {
        super(method, em);
    }

    @Override
    protected Query doCreateQuery(Object[] values) {
        String nativeQuery = getQueryFromTpl(values);
        JpaParameters parameters = getQueryMethod().getParameters();
        ParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
        String sortedQueryString = QueryUtils.applySorting(nativeQuery, accessor.getSort(), QueryUtils.detectAlias(nativeQuery));
        Query query = bind(createJpaQuery(sortedQueryString), values);
        if (parameters.hasPageableParameter()) {
            Pageable pageable = (Pageable) (values[parameters.getPageableIndex()]);
            if (pageable != null) {
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            }
        }
        return query;
    }

    private String getQueryFromTpl(Object[] values) {
        return ApplicationContext.getBean(FreemarkerSqlTemplates.class).process(getQueryMethod(),
                        QueryBuilder.transValuesToDataModel(getQueryMethod().getParameters(), values));
    }



    private Query createJpaQuery(String queryString) {
        Class<?> objectType = getQueryMethod().getReturnedObjectType();

        //get original proxy query.
        Query oriProxyQuery;

        //must be hibernate QueryImpl
        NativeQuery<?> query;

        if (useJpaSpec && getQueryMethod().isQueryForEntity()) {
            oriProxyQuery = getEntityManager().createNativeQuery(queryString, objectType);
        } else {
            oriProxyQuery = getEntityManager().createNativeQuery(queryString);
            query = AopTargetUtils.getTarget(oriProxyQuery);
            Class<?> genericType;
            //find generic type
            if (objectType.isAssignableFrom(Map.class)) {
                genericType = objectType;
            } else {
                ClassTypeInformation<?> ctif = ClassTypeInformation.from(objectType);
                TypeInformation<?> actualType = ctif.getActualType();
                genericType = actualType.getType();
            }
            if (genericType != Void.class) {
                QueryBuilder.transform(query, genericType);
            }
        }
        //return the original proxy query, for a series of JPA actions, e.g.:close em.
        return oriProxyQuery;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected TypedQuery<Long> doCreateCountQuery(Object[] values) {
        TypedQuery<Long> query = (TypedQuery<Long>) getEntityManager().createNativeQuery(QueryBuilder.toCountQuery(getQueryFromTpl(values)));
        bind(query, values);
        return query;
    }

    private Query bind(Query query, Object[] values) {
        //get proxy target if exist.
        //must be hibernate QueryImpl
        NativeQuery<?> targetQuery = AopTargetUtils.getTarget(query);
        Map<String, Object> params = QueryBuilder.transValuesToMap(getQueryMethod().getParameters(), values);
        if (!CollectionUtils.isEmpty(params)) {
            QueryBuilder.setParams(targetQuery, params);
        }
        return query;
    }
}
