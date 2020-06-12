/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * SoMaster.java 2016年11月10日
 */
package org.maculaframework.boot.core.repository.jpa.support.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.maculaframework.boot.core.repository.domain.AbstractAuditable;

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * <b>SoMaster</b> is
 * </p>
 *
 * @since 2016年11月10日
 * @author Rain
 * @version $Id$
 */

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "SO_MASTER")
public class SoMaster extends AbstractAuditable<Long> {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "SO_NO")
	private String soNo;
	
	@Column(name = "SO_NAME")
	private String soName;
	
	@OneToMany(mappedBy = "soMaster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SoDetail> soDetails;
}
