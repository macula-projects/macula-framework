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
package org.macula.boot.core.repository.support;

import org.macula.boot.core.repository.TemplateQuery;
import org.macula.boot.core.repository.support.domain.EmbbedContactInfo;
import org.macula.boot.core.repository.support.domain.User;
import org.macula.boot.core.repository.support.domain.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b>UserRepository</b> 是用户存取接口定义.
 * </p>
 *
 * @author Rain
 * @version $Id: UserRepository.java 5354 2014-09-01 03:21:07Z wzp $
 * @since 2010-12-30
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, UserRepositoryCustom {

    @Query("select new org.macula.core.test.domain.UserVo(u.firstName,u.lastName) from User u where u.lastName = ?1")
    Page<UserVo> findByLastName(String lastName, Pageable pageable);

    @Query("select u.contactInfo from User u where u.lastName = ?1")
    List<EmbbedContactInfo> findContactByLastName(String lastName);

    @TemplateQuery
    Page<UserVo> findByLastNameVo(@Param("lastName") String lastName, Pageable pageable);

    @TemplateQuery
    Page<User> findByLastNameMap(@Param("data") Map<String, Object> data, Pageable pageable);

    @TemplateQuery
    @Modifying
    @Transactional
    void updateFirstName(@Param("lastName") String lastName, @Param("firstName") String firstName);

}
