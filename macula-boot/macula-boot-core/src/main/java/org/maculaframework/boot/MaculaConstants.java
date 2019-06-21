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
package org.maculaframework.boot;

/**
 * <p> <b>MaculaConstants</b> 是所有常量定义类 </p>
 * 
 * @since 2010-12-31
 * @author Rain
 * @version $Id: MaculaConstants.java 5906 2015-10-19 09:40:12Z wzp $
 */
public class MaculaConstants {

	/* 匿名用户 */
	public final static String ANONYMOUS_USER = "ANONYMOUS";

	/* 后台默认用户 */
	public final static String BACKGROUND_USER = "*SYSADM";

	public final static String EXCEPTION_CODE_DATABASE = "database";

	public final static String CONFIG_DATASOURCE_PREFIX = "macula.datasource.druid";

	public final static String CONFIG_JPA_PREFIX = "macula.jpa";

	public final static String CONFIG_JPA_REPOSITORIES_PREFIX = CONFIG_JPA_PREFIX + ".repositories";

    public static final String CONFIG_REDIS_PREFIX = "macula.redis";

	public static final String CONFIG_BOOT_CORE_PREFIX = "macula.config";

	public enum ESCAPE_XSS_LEVEL{

		BASIC("BASIC"),
		BASICWITHIMAGES("BASICWITHIMAGES"),
		SIMPLETEXT("SIMPLETEXT"),
		RELAXED("RELAXED");
		
		private String value;
		ESCAPE_XSS_LEVEL(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
