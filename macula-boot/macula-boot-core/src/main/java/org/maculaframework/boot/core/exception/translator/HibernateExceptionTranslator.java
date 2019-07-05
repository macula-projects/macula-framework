/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.maculaframework.boot.core.exception.translator;

import org.hibernate.HibernateException;
import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.*;
import org.maculaframework.boot.core.exception.HibernateDataAccessException;
import org.maculaframework.boot.core.utils.ExceptionUtils;
import org.maculaframework.boot.core.exception.MaculaException;

import java.sql.BatchUpdateException;

/**
 * <p> <b>HibernateExceptionTranslator</b> 是Hibernate部分异常处理 </p>
 *
 * @author Wilson Luo
 * @version $Id: HibernateExceptionTranslator.java 5877 2015-09-25 06:50:45Z wzp $
 * @since 2011-10-10
 */
public class HibernateExceptionTranslator implements MaculaExceptionTranslator {

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public MaculaException translateExceptionIfPossible(Throwable ex) {
        if (ExceptionUtils.getRecursionCauseException(ex, BatchUpdateException.class) != null) {
            return new HibernateDataAccessException("java.sql.BatchUpdateException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, JDBCConnectionException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.JDBCConnectionException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, SQLGrammarException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.SQLGrammarException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, ConstraintViolationException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.ConstraintViolationException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, LockAcquisitionException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.LockAcquisitionException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, PessimisticLockException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.PessimisticLockException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, QueryTimeoutException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.QueryTimeoutException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, GenericJDBCException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.GenericJDBCException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, DataException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.DataException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, HibernateException.class) != null) {
            return new HibernateDataAccessException("org.hibernate.exception.HibernateException", ex);
        }
        return null;
    }
}
