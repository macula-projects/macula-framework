/**
 * Copyright 2010-2012 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.core.hibernate.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Persistable;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.*;

/**
 * <p> <b>RelationDbTextType</b> 是表示该类型的具体内容将会保存到关系型DB中，面向文本类型 </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: RelationDbTextType.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-3-5
 */
public class RelationDbTextType extends AbstractRelationDbLobType<Text> {

    public RelationDbTextType() {
        super(Text.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    protected Text getInternal(ResultSet rs, String[] names, final Persistable<Long> owner, SharedSessionContractImplementor session) throws HibernateException {

        Text origin = new Text();
        origin.setId(owner.getId().toString());
        return new TextProxy(origin, () -> getJdbcTemplate().queryForObject(getSelectFieldSQL(owner.getId()),
                new SingleColumnRowMapper<String>() {
                    @Override
                    protected Object getColumnValue(ResultSet rs, int index, @SuppressWarnings("rawtypes") Class requiredType) throws SQLException {
                        Clob clob = rs.getClob(index);
                        return clob == null ? null : clob.getSubString(1, (int) clob.length());
                    }
                })
        );
    }

    @Override
    protected void setInternal(PreparedStatement st, Text value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (null != value) {
            StandardBasicTypes.STRING.nullSafeSet(st, value.getContent(), index, session);
        } else {
            StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
        }
    }

}
