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

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * <b>AES</b> AES加解密
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 3807 $ $Date: 2012-11-21 15:31:51 +0800 (周三, 21 十一月 2012) $
 */

public class AES {

	private static Symmetric symmetric;

	private static String keyalg = "AES";
	
	private static String algorithm = "AES";

	protected static Log LOG = LogFactory.getLog(AES.class);

	static {
		symmetric = new Symmetric(keyalg, algorithm, "ECB", "PKCS7Padding", 128);
	}

	/**
	 * 生成密钥字串
	 * 
	 * @return
	 */
	public static String generateKeyStr() {
		try {
			byte[] key = symmetric.generateKey().getEncoded();
			return Base64.encode(key);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 生成字节密钥
	 * 
	 * @return
	 */
	public static byte[] generateKey() {
		try {
			return symmetric.generateKey().getEncoded();
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 加密字串，字串用UTF-8生成字节
	 * 
	 * @param plaintext
	 * @param key
	 * @return
	 */
	public static String encrypt(String plaintext, String key) {
		try {
			return symmetric.encrypt(plaintext, str2key(key));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static byte[] encrypt(byte[] plaintext, byte[] key) {
		try {
			return symmetric.encrypt(plaintext, byte2key(key));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 解密字串，用UTF-8生成字串
	 * 
	 * @param cryptotext
	 * @param key
	 * @return
	 */
	public static String decrypt(String cryptotext, String key) {
		try {
			return symmetric.decrypt(cryptotext, str2key(key));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static byte[] decrypt(byte[] cryptotext, byte[] key) {
		try {
			return symmetric.decrypt(cryptotext, byte2key(key));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 将字串转换为KEY
	 * 
	 * @param key
	 * @return
	 */
	public static Key str2key(String skey) {
		try {
			return byte2key(Base64.decode(skey));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static Key byte2key(byte[] bkey) {
		return new SecretKeySpec(bkey, keyalg);
	}

	public static void main(String[] args) {
		try {
			String key = generateKeyStr();
			System.out.println("密钥:" + key);
			//String str = "abc中文DES";
			String str = "71f27b6b-98a4-4f46-954a-af5fc49de6f0";
			System.out.println("明文:" + str);
			String cryptotext = encrypt(str, key);
			System.out.println("密文:" + cryptotext);
			String plaintext = decrypt(cryptotext, key);
			System.out.println("解密:" + plaintext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
