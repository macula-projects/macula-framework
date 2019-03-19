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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * <b>RSAWithSHA1</b> RSAWithSHA签名算法
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class RSAWithSHA1 {

	private static Signature signature;

	private static String keyalg = "RSA";

	private static String algorithm = "SHA1withRSA";

	protected static Log LOG = LogFactory.getLog(RSAWithSHA1.class);

	static {
		signature = new Signature(keyalg, algorithm, 1024);
	}

	/**
	 * 生成密钥字串
	 */
	public static String[] generateKeyPairStr() {
		try {
			KeyPair keyPair = signature.generateKeyPair();
			Key publicKey = keyPair.getPublic();
			Key privateKey = keyPair.getPrivate();
			String publicXML = EncryptionHelper.getRSAPublicKeyAsXML((RSAPublicKey) publicKey);
			return new String[] { Base64.encode(publicKey.getEncoded()), Base64.encode(privateKey.getEncoded()), publicXML };
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
			KeyPair keyPair = signature.generateKeyPair();
			Key publicKey = keyPair.getPublic();
			Key privateKey = keyPair.getPrivate();
			return new byte[][] { publicKey.getEncoded(), privateKey.getEncoded() };
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 签名字串
	 * 
	 * @param plaintext 明文
	 * @param privateKey 私钥
	 */
	public static String sign(String plaintext, String privateKey) {
		try {
			return signature.sign(plaintext, (PrivateKey) str2key(privateKey, false));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 签名字节数组
	 * 
	 * @param plaintext 明文字节数组
	 * @param privateKey 私钥
	 */
	public static byte[] sign(byte[] plaintext, byte[] privateKey) {
		try {
			return signature.sign(plaintext, (PrivateKey) byte2key(privateKey, false));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 验证签名
	 * 
	 * @param plaintext 明文
	 * @param signed 签名字符串
	 * @param publicKey 公钥
	 */
	public static boolean verify(String plaintext, String signed, String publicKey) {
		try {
			return signature.verify(plaintext, signed, (PublicKey) str2key(publicKey, true));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return false;
	}

	/**
	 * 验证签名
	 * 
	 * @param plaintext 明文
	 * @param signed 签名字符串
	 * @param publicKey 公钥
	 */
	public static boolean verify(byte[] plaintext, byte[] signed, byte[] publicKey) {
		try {
			return signature.verify(plaintext, signed, (PublicKey) byte2key(publicKey, true));
		} catch (Exception e) {
			LOG.debug(e);
		}
		return false;
	}

	/**
	 * 将字串转换为KEY
	 * 
	 * @param skey 字符串key，Base64
	 * @param publicKey 是否是公钥
	 */
	public static Key str2key(String skey, boolean publicKey) {
		try {
			return byte2key(Base64.decode(skey), publicKey);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	/**
	 * 将字节数组转换为KEY
	 * 
	 * @param bkey 字节数组key
	 * @param publicKey 是否是公钥
	 */
	public static Key byte2key(byte[] bkey, boolean publicKey) {
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
			String str = "abc中文SHA1withRSAabc中文SHA1withRSAabc中文SHA1withRSAabc中文SHA1withRSA";
			System.out.println("明文:" + str);

			String signed = sign(str, keys[1]);
			System.out.println("签名:" + signed);

			boolean result = verify(str, signed, keys[0]);
			System.out.println("验证:" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
