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

package org.springframework.data.jpa.repository.query;

import org.springframework.data.spel.EvaluationContextProvider;
import org.springframework.data.util.StreamUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * <b>TemplateQueryParameterBinderFactory</b> 模板查询的参数绑定工厂
 * </p>
 *
 * @author Rain
 * @since 2019-01-27
 */
public class TemplateQueryParameterBinderFactory {

    public static ParameterBinder createQueryAwareBinder(JpaParameters parameters, DeclaredQuery query,
                                                         ExpressionParser parser, EvaluationContextProvider evaluationContextProvider) {

        Assert.notNull(parameters, "JpaParameters must not be null!");
        Assert.notNull(query, "StringQuery must not be null!");
        Assert.notNull(parser, "ExpressionParser must not be null!");
        Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");

        List<StringQuery.ParameterBinding> bindings = query.getParameterBindings();
        QueryParameterSetterFactory expressionSetterFactory = TemplateBasedQueryParameterSetterFactory.parsingTemplate(parser,
                evaluationContextProvider, parameters);
        QueryParameterSetterFactory basicSetterFactory = TemplateBasedQueryParameterSetterFactory.basic(parameters);

        return new ParameterBinder(parameters, createSetters(bindings, query, expressionSetterFactory, basicSetterFactory),
                !query.usesPaging());
    }


    private static Iterable<QueryParameterSetter> createSetters(List<StringQuery.ParameterBinding> parameterBindings,
                                                                QueryParameterSetterFactory... factories) {
        return createSetters(parameterBindings, EmptyDeclaredQuery.EMPTY_QUERY, factories);
    }

    private static Iterable<QueryParameterSetter> createSetters(List<StringQuery.ParameterBinding> parameterBindings,
                                                                DeclaredQuery declaredQuery, QueryParameterSetterFactory... strategies) {

        return parameterBindings.stream() //
                .map(it -> createQueryParameterSetter(it, strategies, declaredQuery)) //
                .collect(StreamUtils.toUnmodifiableList());
    }

    private static QueryParameterSetter createQueryParameterSetter(StringQuery.ParameterBinding binding,
                                                                   QueryParameterSetterFactory[] strategies, DeclaredQuery declaredQuery) {

        return Arrays.stream(strategies)//
                .map(it -> it.create(binding, declaredQuery))//
                .filter(Objects::nonNull)//
                .findFirst()//
                .orElse(QueryParameterSetter.NOOP);
    }
}
