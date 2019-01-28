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
package org.macula.boot.core.repository.support.domain;

import org.hibernate.annotations.*;
import org.macula.boot.core.domain.AbstractAuditable;
import org.macula.boot.core.hibernate.audit.Auditable;
import org.macula.boot.core.hibernate.type.Binary;
import org.macula.boot.core.hibernate.type.MongoDbBinaryType;
import org.macula.boot.core.hibernate.type.MongoDbTextType;
import org.macula.boot.core.hibernate.type.Text;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p> <b>User</b> 是用户测试模型. </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: UserMongo.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2010-12-30
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MY_USER_MONGO")
@Auditable
@TypeDefs({@TypeDef(name = "mongoBinary", typeClass = MongoDbBinaryType.class),
        @TypeDef(name = "mongoText", typeClass = MongoDbTextType.class)})
public class UserMongo extends AbstractAuditable<Long> {

    private static final long serialVersionUID = 1L;

    @javax.validation.constraints.Size(min = 1)
    @Auditable
    @Column(name = "FIRST_NAME")
    private String firstname;
    @javax.validation.constraints.Size(max = 10)
    @Auditable
    @Column(name = "LAST_NAME")
    private String lastname;
    @Auditable
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHOTO")
    @Type(type = "mongoBinary", parameters = {@Parameter(name = "mongoCollectionName", value = "UserMongo_Photo")})
    private Binary photo;

    @Column(name = "PROFILE")
    @Type(type = "mongoText", parameters = {@Parameter(name = "mongoCollectionName", value = "UserMongo_Profile")})
    private Text profile;

    public UserMongo() {
        this(null);
    }

    public UserMongo(Long id) {
        this.setId(id);
    }

    public Text getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        if (this.profile == null) {
            this.profile = new Text();
        }
        this.profile.setContent(profile);
    }

    /**
     * @return the photo
     */
    public Binary getPhoto() {
        return photo;
    }

    /**
     * @param photo the photo to set
     */
    public void setPhoto(byte[] photo) {
        if (this.photo == null) {
            this.photo = new Binary();
        }
        this.photo.setContent(photo);
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
