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
package org.macula.boot.utils.certs;

/**
 * <p>
 * <b>CertConfig</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 4509 $ $Date: 2013-10-23 18:08:48 +0800 (三, 23 10 2013) $
 */
public class CertConfig {

	public static String KEY_ALG = "RSA";

	public static String SIGN_ALG = "SHA1withRSA";

	public static int KEY_SIZE = 1024;

	/*-------------------- CA 配置 ------------------------------*/
	public static String BASIC_ISSUE = "C=CN,ST=Guangdong,L=Guangzhou,O=Infinitus(China) Company Ltd,";

	// 密码好像不能超过7位，不知道为什么?
	public static String CA_STORE_PASS = "infi345";

	public static String CA_OU = "SOA";

	public static String CA_CN = "Infinitus(China) Company Ltd Websphere MQ Root CA";

	public static String CA_EMAIL = "ca@infinitus-int.com";

	public static int CA_LIMIT_DAY = 30 * 365;

	/*------------------ Server 证书配置 ------------------------*/
	public static String SERVER_PASS = "infinitus345";

	public static String SERVER_OU = "SOA";

	public static String SERVER_CN = "mqgbsstest";

	public static String SERVER_EMAIL = "mq@infinitus-int.com";

	public static int SERVER_LIMIT_DAY = 10 * 365;

	/*------------------ Client 证书配置 -----------------------*/
	public static String CLIENT_PASS = "infc345";

	public static String CLIENT_OU = "SOA";

	public static String CLIENT_CN = "mqgbsstest";

	public static String CLIENT_EMAIL = "mqcsmtest@infinitus-int.com";

	public static int CLIENT_LIMIT_DAY = 10 * 365;
}
