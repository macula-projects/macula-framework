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
package org.maculaframework.boot.core.repository.jpa.support;

import org.maculaframework.boot.core.repository.jpa.JpaEntityManagerAware;
import org.maculaframework.boot.core.repository.jpa.support.domain.User;
import org.maculaframework.boot.core.repository.jpa.support.domain.UserVo2;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * <b>UserRepositoryCustom</b> 是用户存取自定义接口.
 * </p>
 *
 * @author Rain
 * @version $Id: UserRepositoryCustom.java 5351 2014-08-27 09:19:53Z wzp $
 * @since 2011-2-15
 */

@Transactional(readOnly = true)
public interface UserRepositoryCustom extends JpaEntityManagerAware {
    @Transactional
    User loadElementById(Long id);

    @Transactional
    User saveUser(User user);

    @Transactional
    void customDelete(User entity);

    List<UserVo2> findByXXX(String lastName);
}
