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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.macula.boot.MaculaConstants;
import org.macula.boot.core.domain.AbstractAuditable;
import org.macula.boot.core.hibernate.audit.Auditable;
import org.macula.boot.core.hibernate.type.Binary;
import org.macula.boot.core.hibernate.type.RelationDbBinaryType;
import org.macula.boot.core.hibernate.type.RelationDbTextType;
import org.macula.boot.core.hibernate.type.Text;

/**
 * <p>
 * <b>User</b> 是用户测试模型.
 * </p>
 * 
 * @since 2010-12-30
 * @author Rain
 * @author Wilson Luo
 * @version $Id: User.java 5354 2014-09-01 03:21:07Z wzp $
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MY_USER")
@Auditable
@TypeDefs({ @TypeDef(name = "binary", typeClass = RelationDbBinaryType.class),
		@TypeDef(name = "text", typeClass = RelationDbTextType.class) })
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
	@Column(name = "PHOTO", columnDefinition = "LONGVARBINARY")
	@Type(type = "binary", parameters = {
			@Parameter(name = "columnName", value = "PHOTO"),
			@Parameter(name = "jdbcTemplate", value = MaculaConstants.JDBC_TEMPLATE_NAME),
			@Parameter(name = "tableName", value = "MY_USER") })
	private Binary photo;
	@Column(name = "PROFILE", columnDefinition = "CLOB")
	@Type(type = "text", parameters = {
			@Parameter(name = "columnName", value = "PROFILE"),
			@Parameter(name = "jdbcTemplate", value = MaculaConstants.JDBC_TEMPLATE_NAME),
			@Parameter(name = "tableName", value = "MY_USER") })
	private Text profile;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "homeTel", column = @Column(name = "HOME_TEL")),
			@AttributeOverride(name = "officeTel", column = @Column(name = "OFFICE_TEL")) })
	private EmbbedContactInfo contactInfo;

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
	 * @param photo
	 *            the photo to set
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
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User() {
		this(null);
	}

	public User(Long id) {
		this.setId(id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the contactInfo
	 */
	public EmbbedContactInfo getContactInfo() {
		return contactInfo;
	}

	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(EmbbedContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
}
