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
package org.macula.boot.utils.encryption;

import java.security.MessageDigest;

/**
 * <p>
 * <b>Digest</b> 生成摘要
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class Digest {

	private String algorithm;

	public Digest(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * 产生散列值
	 * 
	 * @param bytes 需要散列的字节数组
	 * @throws Exception
	 */
	public String gen(byte[] bytes) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(bytes);
			byte[] digests = md.digest();
			return EncryptionHelper.byte2hex(digests);
		} catch (Exception e) {
			throw new Exception("非法摘要算法");
		}
	}

	/**
	 * 产生散列值
	 * 
	 * @param str 需要散列的字符串，用UTF-8转为字节数组
	 * @throws Exception
	 */
	public String gen(String str) throws Exception {
		return gen(str.getBytes("UTF-8"));
	}
}
