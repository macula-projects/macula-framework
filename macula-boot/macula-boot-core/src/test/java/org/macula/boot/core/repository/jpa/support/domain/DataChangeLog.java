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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.springframework.data.domain.Persistable;

/**
 * <p> <b>DataChangeLog</b> 是变化日志保存表. </p>
 * 
 * @since 2011-4-11
 * @author Wilson Luo
 * @version $Id: DataChangeLog.java 3807 2012-11-21 07:31:51Z wilson $
 */

@Data
@Entity
@Table(name = "DATA_CHG_LOG")
public class DataChangeLog implements Persistable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 15)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 变化的表名 */
	private String tableName;

	/** 变化的字段名 */
	private String columnName;

	/** 变化的数据ID */
	private String dataId;

	/** 变化前的数据 */
	private String oldValue;

	/** 变化后的数据 */
	private String newValue;

	/** 变化批次 */
	private String batchNo;

	/** 变更人 */
	private String modifiedBy;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
