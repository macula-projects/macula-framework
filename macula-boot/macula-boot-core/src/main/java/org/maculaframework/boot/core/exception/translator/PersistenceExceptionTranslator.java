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

import org.maculaframework.boot.core.exception.JpaDataAccessException;
import org.maculaframework.boot.core.utils.ExceptionUtils;
import org.maculaframework.boot.core.exception.MaculaException;

import javax.persistence.*;

/**
 * <p>
 * <b>PersistenceExceptionTranslator</b> is JPA的异常翻译
 * </p>
 *
 * @since 2011-10-28
 * @author zhengping_wang
 * @version $Id: PersistenceExceptionTranslator.java 5877 2015-09-25 06:50:45Z wzp $
 */
public class PersistenceExceptionTranslator implements MaculaExceptionTranslator {

    @Override
    public int getOrder() {
        return 200;
    }

    @Override
    public MaculaException translateExceptionIfPossible(Throwable ex) {
        if (ExceptionUtils.getRecursionCauseException(ex, TransactionRequiredException.class) != null) {
            return new JpaDataAccessException("javax.persistence.TransactionRequiredException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, RollbackException.class) != null) {
            return new JpaDataAccessException("javax.persistence.RollbackException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, OptimisticLockException.class) != null) {
            return new JpaDataAccessException("javax.persistence.OptimisticLockException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, NoResultException.class) != null) {
            return new JpaDataAccessException("javax.persistence.NoResultException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, NonUniqueResultException.class) != null) {
            return new JpaDataAccessException("javax.persistence.NonUniqueResultException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, EntityNotFoundException.class) != null) {
            return new JpaDataAccessException("javax.persistence.EntityNotFoundException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, EntityExistsException.class) != null) {
            return new JpaDataAccessException("javax.persistence.EntityExistsException", ex);
        }
        if (ExceptionUtils.getRecursionCauseException(ex, PersistenceException.class) != null) {
            return new JpaDataAccessException("javax.persistence.PersistenceException", ex);
        }
        return null;
    }

}
