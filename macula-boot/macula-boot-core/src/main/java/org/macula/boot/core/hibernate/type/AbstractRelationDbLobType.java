/**
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.core.hibernate.type;

import java.util.Properties;

import org.macula.boot.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * <p> <b>AbstractRelationDbLobType</b> 是抽象的大字段存储到关系数据库的实现. </p>
 * 
 * @since 2011-3-7
 * @author Rain
 * @author Wilson Luo
 * @version $Id: AbstractRelationDbLobType.java 5584 2015-05-18 07:54:35Z wzp $
 */
public abstract class AbstractRelationDbLobType<T extends Lob> extends AbstractLobType<T> {

	private static final String JDBC_TEMPLATE_ATTR = "jdbcTemplate";
	private static final String TABLE_NAME_ATTR = "tableName";
	private static final String COLUMN_NAME_ATTR = "columnName";
	private static final String PK_ATTR = "pk";

	private static final String DEFAULT_JDBC_TEMPLATE_VALUE = "jdbcTemplate";
	private static final String DEFAULT_PK_VALUE = "id";
	private String jdbcTemplateName;
	private String tableName;
	private String columnName;
	private String pkName;

	private JdbcTemplate jdbcTemplate;

	protected AbstractRelationDbLobType(Class<T> clazz) {
		super(clazz);
	}

	@Override
	protected void initParameterValues(Properties properties) {
		if (properties != null) {
			jdbcTemplateName = properties.getProperty(JDBC_TEMPLATE_ATTR);
			pkName = properties.getProperty(PK_ATTR);
			tableName = properties.getProperty(TABLE_NAME_ATTR);
			columnName = properties.getProperty(COLUMN_NAME_ATTR);
		}
		if (StringUtils.isEmpty(jdbcTemplateName)) {
			jdbcTemplateName = DEFAULT_JDBC_TEMPLATE_VALUE;
		}
		if (StringUtils.isEmpty(pkName)) {
			pkName = DEFAULT_PK_VALUE;
		}
	}

	/**
	 * 延迟初始化mongoTemplate。
	 */
	protected synchronized JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null && !StringUtils.isEmpty(jdbcTemplateName)) {
			jdbcTemplate = ApplicationContext.getBean(jdbcTemplateName);
		}
		return jdbcTemplate;
	}

	protected String getSelectFieldSQL(Long id) {
		return "select " + columnName + " from " + tableName + " where " + pkName + " = " + id;
	}

}
