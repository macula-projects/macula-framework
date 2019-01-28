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

import org.macula.boot.core.repository.support.domain.UserMongo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * <b>UserRepository</b> 是用户存取接口定义.
 * </p>
 *
 * @author Rain
 * @version $Id: UserMongoRepository.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2010-12-30
 */
public interface UserMongoRepository extends JpaRepository<UserMongo, Long>, JpaSpecificationExecutor<UserMongo>, UserMongoRepositoryCustom {

}
