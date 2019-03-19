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
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * <b>RSA</b> RSA加解密
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class RSA {

	private static Asymmetric asymmetric;

	private static String keyalg = "RSA";

	private static String algorithm = "RSA";

	protected static Log LOG = LogFactory.getLog(RSA.class);

	static {
		asymmetric = new Asymmetric(keyalg, algorithm, "ECB", "PKCS1Padding", 1024);
	}

	/**
	 * 生成密钥字串
	 */
	public static String[] generateKeyPairStr() {
		try {
			KeyPair keyPair = asymmetric.generateKeyPair();
			Key publicKey = keyPair.getPublic();
			Key privateKey = keyPair.getPrivate();
			String publicXML = EncryptionHelper.getRSAPublicKeyAsXML((RSAPublicKey) publicKey);
			return new String[] {Base64.encode(publicKey.getEncoded()),
					Base64.encode(privateKey.getEncoded()), publicXML };
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 生成字节密钥
	 */
	public static byte[][] generateKeyPair() {
		try {
			KeyPair keyPair = asymmetric.generateKeyPair();
			Key publicKey = keyPair.getPublic();
			Key privateKey = keyPair.getPrivate();
			return new byte[][] { publicKey.getEncoded(), privateKey.getEncoded() };
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 加密字串，字串用UTF-8生成字节
	 * 
	 * @param plaintext 明文
	 * @param publicKey 公钥
	 */
	public static String encrypt(String plaintext, String publicKey) {
		try {
			return asymmetric.encrypt(plaintext, str2key(publicKey, true));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 加密字节数组
	 * @param plaintext 明文字节数组
	 * @param publicKey 公钥
	 */
	public static byte[] encrypt(byte[] plaintext, byte[] publicKey) {
		try {
			return asymmetric.encrypt(plaintext, byte2key(publicKey, true));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 解密字串，用UTF-8生成字串
	 * 
	 * @param cryptotext 密文
	 * @param privateKey 私钥
	 */
	public static String decrypt(String cryptotext, String privateKey) {
		try {
			return asymmetric.decrypt(cryptotext, str2key(privateKey, false));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 解密字节数组
	 * @param cryptotext 密文字节数组
	 * @param privateKey 私钥
	 */
	public static byte[] decrypt(byte[] cryptotext, byte[] privateKey) {
		try {
			return asymmetric.decrypt(cryptotext, byte2key(privateKey, false));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	// 将字串转换为KEY
	private static Key str2key(String skey, boolean publicKey) {
		try {
			return byte2key(Base64.decode(skey), publicKey);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	private static Key byte2key(byte[] bkey, boolean publicKey) {
		try {
			KeyFactory fact = KeyFactory.getInstance(keyalg);
			if (publicKey) {
				KeySpec keySpec = new X509EncodedKeySpec(bkey);
				return fact.generatePublic(keySpec);
			} else {
				KeySpec keySpec = new PKCS8EncodedKeySpec(bkey);
				return fact.generatePrivate(keySpec);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug(e);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			String[] keys = generateKeyPairStr();
			System.out.println("公钥:" + keys[0]);
			System.out.println("私钥:" + keys[1]);
			System.out.println("XML:" + keys[2]);
			String str = "abc中文RSA";
			System.out.println("明文:" + str);
			String cryptotext = encrypt(str, keys[0]);
			System.out.println("密文:" + cryptotext);
			String plaintext = decrypt(cryptotext, keys[1]);
			System.out.println("解密:" + plaintext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
