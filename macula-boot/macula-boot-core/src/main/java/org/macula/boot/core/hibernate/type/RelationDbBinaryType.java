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
 * <p> <b>RelationDbBinaryType</b> 是表示该类型的具体内容将会保存到关系型DB中，面向二进制类型 </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: RelationDbBinaryType.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-3-5
 */
public class RelationDbBinaryType extends AbstractRelationDbLobType<Binary> {

    public RelationDbBinaryType() {
        super(Binary.class);

    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARBINARY};
    }

    @Override
    protected Binary getInternal(ResultSet rs, String[] names, final Persistable<Long> owner, SharedSessionContractImplementor session)
            throws HibernateException {

        Binary origin = new Binary();
        origin.setId(owner.getId().toString());
        return new BinaryProxy(origin, () -> getJdbcTemplate().queryForObject(getSelectFieldSQL(owner.getId()),
                new SingleColumnRowMapper<byte[]>() {
                    @Override
                    protected Object getColumnValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
                        Blob blob = rs.getBlob(index);
                        return blob == null ? null : blob.getBytes(1, (int) blob.length());
                    }
                })
        );
    }

    @Override
    protected void setInternal(PreparedStatement st, Binary value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (null != value) {
            StandardBasicTypes.BINARY.nullSafeSet(st, value.getContent(), index, session);
        } else {
            StandardBasicTypes.BINARY.nullSafeSet(st, null, index, session);
        }
    }

}
