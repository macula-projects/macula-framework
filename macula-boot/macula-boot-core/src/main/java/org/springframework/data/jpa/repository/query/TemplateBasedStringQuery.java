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

import org.springframework.expression.ExpressionParser;

/**
 * <p>
 * <b>TemplateBasedStringQuery</b> 基于模板的Query
 * </p>
 *
 * @author Rain
 * @since 2019-01-27
 */
public class TemplateBasedStringQuery extends StringQuery {
    private JpaEntityMetadata<?> entityInformation;
    private ExpressionParser parser;

    /**
     * Creates a new {@link StringQuery} from the given JPQL query.
     *
     * @param query must not be {@literal null} or empty.
     */
    TemplateBasedStringQuery(String query) {
        super(query);
    }

    public TemplateBasedStringQuery(String query, JpaEntityMetadata<?> entityInformation, ExpressionParser parser) {
        this(query);
        this.entityInformation = entityInformation;
        this.parser = parser;
    }
}
