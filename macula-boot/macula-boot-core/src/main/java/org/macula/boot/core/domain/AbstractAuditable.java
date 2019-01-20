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
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.Auditable;

/**
 * Abstract base class for auditable entities. Stores the audition values in
 * persistent fields.
 *
 * @param <PK> the type of the auditing type's idenifier
 * @author Oliver Gierke
 */
@MappedSuperclass
public abstract class AbstractAuditable<PK extends Serializable> extends AbstractPersistable<PK> implements
        Auditable<String, PK, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "LAST_UPDATED_BY", nullable = false, length = 50)
    private String lastModifiedBy;

    @Column(name = "LAST_UPDATED_TIME", nullable = false)
    private LocalDateTime lastModifiedDate;

    public Optional<String> getCreatedBy() {
        return Optional.of(createdBy);
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public Optional<LocalDateTime> getCreatedDate() {
        return Optional.of(createdDate);
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Optional<String> getLastModifiedBy() {
        return Optional.of(lastModifiedBy);
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.of(lastModifiedDate);
    }

    public void setLastModifiedDate(final LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
