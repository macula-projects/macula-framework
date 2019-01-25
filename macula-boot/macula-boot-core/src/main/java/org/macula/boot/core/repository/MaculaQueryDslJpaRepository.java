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
package org.macula.boot.core.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * <p>
 * <b>MaculaQueryDslJpaRepository</b> 扩展了{@link QueryDslJpaRepository}，添加获取{@link EntityManager}的方法
 * </p>
 *
 * @author Rain
 * @version $Id: MaculaQueryDslJpaRepository.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-4-19
 */
public class MaculaQueryDslJpaRepository<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements
        MaculaJpaRepository<T, ID> {

    private EntityManager em;

    /**
     * @param entityMetadata
     * @param entityManager
     */
    public MaculaQueryDslJpaRepository(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {
        super(entityMetadata, entityManager);
        this.em = entityManager;
    }

    /* (non-Javadoc)
     * @see org.macula.core.repository.MaculaJpaRepository#getEntityManager(javax.persistence.EntityManager)
     */
    @Override
    public EntityManager getEntityManager() {
        return this.em;
    }


}
