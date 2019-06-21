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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.maculaframework.boot.core.repository.jpa.support.domain.User;
import org.maculaframework.boot.core.repository.jpa.support.domain.UserVo2;

/**
 * <p>
 * <b>UserRepositoryImpl</b> 是用户存取自定义方法实现.
 * </p>
 *
 * @since 2011-2-15
 * @author Rain
 * @version $Id: UserRepositoryImpl.java 5663 2015-06-19 09:49:03Z wzp $
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

	private EntityManager em;

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
	public User loadElementById(Long id) {
		return em.find(User.class, id);
	}

	/* (non-Javadoc)
	 * @see org.macula.core.repository.UserRepositoryCustom#saveUser(org.macula.core.domain.User)
	 */
	@Override
	public User saveUser(User user) {
		if (null == user.getId()) {
			em.persist(user);
		} else {
			em.merge(user);
		}
		return user;
	}

	@Override
	public void customDelete(User entity) {
		if (entity != null) {
			em.remove(em.contains(entity) ? entity : em.merge(entity));
		}
	}

	/* (non-Javadoc)
	 * @see org.macula.core.test.repository.UserRepositoryCustom#findByXXX(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	@SuppressWarnings({"deprecation", "unchecked"})
	public List<UserVo2> findByXXX(String lastName) {
		Query q = em.createQuery("select u.firstName as firstName, u.lastName as lastName from User u where u.lastName=:lastName");
		return ((org.hibernate.query.Query<UserVo2>)q).setResultTransformer(new AliasToBeanResultTransformer(UserVo2.class))
				.setParameter("lastName", lastName)
				.list();
	}

}
