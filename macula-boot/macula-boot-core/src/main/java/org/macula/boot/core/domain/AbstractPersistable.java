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
package org.macula.boot.core.domain;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

/**
 * Abstract base class for entities. Allows parameterization of id type, chooses
 * auto-generation and implements {@link #equals(Object)} and
 * {@link #hashCode()} based on that id.
 *
 * @param <PK> the the of the entity
 * @author Oliver Gierke
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {


    @Id
    @Column(name = "ID", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private PK id;

    /**
     * 是否已删除标志
     */
    private transient boolean deleted = false;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Nullable
    public PK getId() {
        return id;
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    protected void setId(@Nullable PK id) {
        this.id = id;
    }

    /**
     * Must be {@link Transient} in order to ensure that no JPA provider complains because of a missing setter.
     *
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Transient // DATAJPA-622
    public boolean isNew() {
        return null == getId();
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        }

        AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }
}