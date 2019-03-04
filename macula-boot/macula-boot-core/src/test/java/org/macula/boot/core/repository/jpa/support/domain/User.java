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
package org.macula.boot.core.repository.jpa.support.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.macula.boot.core.repository.domain.AbstractAuditable;
import org.macula.boot.core.repository.hibernate.audit.Auditable;

import javax.persistence.*;

/**
 * <p>
 * <b>User</b> 是用户测试模型.
 * </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: User.java 5354 2014-09-01 03:21:07Z wzp $
 * @since 2010-12-30
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MY_USER")
@Auditable
@Data
public class User extends AbstractAuditable<Long> {

    private static final long serialVersionUID = 1L;

    @javax.validation.constraints.Size(min = 1, max = 10)
    @Auditable
    @Column(name = "FIRST_NAME")
    private String firstName;
    @javax.validation.constraints.Size(max = 10)
    @Auditable
    @Column(name = "LAST_NAME")
    private String lastName;
    @Auditable
    @Column(name = "EMAIL")
    private String email;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "homeTel", column = @Column(name = "HOME_TEL")),
            @AttributeOverride(name = "officeTel", column = @Column(name = "OFFICE_TEL"))})
    private EmbbedContactInfo contactInfo;
}
