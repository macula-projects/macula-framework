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
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p> <b>AbstractLobType</b> 是大字段类型的抽象实现，用于domain的定义，依赖Hibernate </p>
 *
 * @since 2011-3-9
 * @author Wilson Luo
 * @version $Id: AbstractLobType.java 3807 2012-11-21 07:31:51Z wilson $
 */
public abstract class AbstractLobType<T extends Lob> implements UserType, ParameterizedType {

    private final Class<T> lobType;

    protected AbstractLobType(Class<T> clazz) {
        this.lobType = clazz;
    }

    @Override
    public Class<T> returnedClass() {
        return lobType;
    }

    @Override
    public final T nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return getInternal(rs, names, (Persistable<Long>) owner, session);
    }

    protected abstract T getInternal(ResultSet rs, String[] names, Persistable<Long> owner, SharedSessionContractImplementor session)
            throws HibernateException, SQLException;

    @Override
    public final void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException,
            SQLException {
        setInternal(st, (T) value, index, session);
    }

    protected abstract void setInternal(PreparedStatement st, T value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException;

    public final void setParameterValues(Properties properties) {
        initParameterValues(properties);
    }

    protected abstract void initParameterValues(Properties properties);

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        if (cached == null) {
            return null;
        }
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        return (Serializable) deepCopy(value);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x != null) {
            return x.equals(y);
        }
        if (y != null) {
            return y.equals(x);
        }
        if (x == null && y == null) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

}
