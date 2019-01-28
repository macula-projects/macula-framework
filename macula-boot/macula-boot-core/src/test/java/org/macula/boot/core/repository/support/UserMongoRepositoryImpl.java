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

import javax.persistence.EntityManager;

import org.macula.core.hibernate.type.Binary;
import org.macula.core.test.domain.UserMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * <p>
 * <b>UserRepositoryImpl</b> 是用户存取自定义方法实现.
 * </p>
 *
 * @since 2011-2-15
 * @author Rain
 * @version $Id: UserMongoRepositoryImpl.java 3807 2012-11-21 07:31:51Z wilson $
 */
public class UserMongoRepositoryImpl implements UserMongoRepositoryCustom {

	private EntityManager em;

	@Autowired
	private MongoTemplate mongoTemplate;

	/* (non-Javadoc)
	 * @see org.macula.core.repository.JpaEntityManagerAware#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	/* (non-Javadoc)
	 * @see org.macula.core.repository.UserRepositoryCustom#loadElementById(java.lang.Long)
	 */
	@Override
	public UserMongo loadElementById(Long id) {
		return em.find(UserMongo.class, id);
	}

	/* (non-Javadoc)
	 * @see org.macula.core.repository.UserRepositoryCustom#saveUser(org.macula.core.domain.User)
	 */
	@Override
	public UserMongo saveUser(UserMongo user) {
		if (null == user.getId()) {
			em.persist(user);
		} else {
			em.merge(user);
		}
		return user;
	}

	@Override
	public void customDelete(UserMongo entity) {
		if (entity != null) {
			Binary binary = entity.getPhoto();
			if (binary != null) {
				mongoTemplate.remove(new Query(Criteria.where("id").is(binary.getId())), Binary.class);
			}
			em.remove(em.contains(entity) ? entity : em.merge(entity));
		}
	}

}
