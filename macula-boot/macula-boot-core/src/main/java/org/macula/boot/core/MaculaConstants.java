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
package org.macula.boot.core;

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

	/* Jdbc Template Name */
	public final static String JDBC_TEMPLATE_NAME = "jdbcTemplate_macula";

	/** macula admin模块的父错误码 */
	public final static String MACULA_ADMIN_ERROR_CODE = "macula.admin";

	/** macula core模块的父错误码 */
	public final static String MACULA_CORE_ERROR_CODE = "macula.core";

	/** macula base模块的父错误码 */
	public final static String MACULA_BASE_ERROR_CODE = "macula.base";
	
	/** Email發送和接收錯誤碼 */
	public final static String MACULA_CODE_MAIL = "mail";

	/** 微信收发错误码 */
	public final static String MACULA_CODE_WECHAT = "wechat";

	/** 请求参数错误码 */
	public final static String MACULA_BASE_PARAM_CODE = "param";

	/** HTTP异常 */
	public final static String MACULA_BASE_HTTP_CODE = "http";
	
	/** 事件广播方式 */
	public final static String BROAD_CAST_TYPE_NONE = "none";
	
	public final static String BROAD_CAST_TYPE_REDIS = "redis";
	
	public final static String BROAD_CAST_TYPE_INTEGRATION = "integration";

	/** 存放旧密码次数 */
	public static final int PASSWORD_HISTORY_NUM = 5;

	/** 系统运行模式 */
	public final static String RUN_MODE_DEV = "dev";

	public final static String RUN_MODE_TEST = "test";

	public final static String RUN_MODE_PRD = "prd";
	
	// 终端类型
	public static final String TERMINAL_PC = "PC";// PC
	public static final String TERMINAL_MOBILE = "MOBILE"; // 手机 
	public static final String TERMINAL_KIOSK = "KIOSK"; // 自助终端

	// 密码验证方式
	public static final String AUTH_TYPE_PASSWORD = "password";// password
	public static final String AUTH_TYPE_REMEMBERME = "rememberme"; // remember me
	public static final String AUTH_TYPE_DYNAMIC_CODE = "dyna_code";// dynamic code
	public static final String AUTH_TYPE_SERVICE_PASSWORD = "service_pass";// business password
	public static final String AUTH_TYPE_UNKNOWN = "unknown";
	
	// Mower UI
	public static final String UI_MOWER = "mower";

	public static final String UI_DEFAULT = "default";
	
	public static final String DATABASE_ORACLE = "ORACLE";
	
	public static final String DATABASE_MYSQL = "MYSQL";
	
	public static final String VARIABLE_ADMIN = "admin";
	public static final String VARIABLE_FRONT = "front";
	public static final String VARIABLE_MOBILE = "mobile";
	
	public static final String PROFILE_LOCAL = "local";
	public static final String PROFILE_DEV = "dev";
	
	public static final String MACULA_CURATOR_FRAMEWORK_BEAN_NAME = "maculaCuratorFramework";
	
	public enum ESCAPE_XSS_LEVEL{

		BASIC("BASIC"),
		BASICWITHIMAGES("BASICWITHIMAGES"),
		SIMPLETEXT("SIMPLETEXT"),
		RELAXED("RELAXED");
		
		private String value;
		private ESCAPE_XSS_LEVEL(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
