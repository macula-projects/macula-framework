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

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * <b>Symmetric</b> 非对称加密算法
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class Asymmetric {

	private String keyalg; // 密钥产生算法

	private String algorithm; // 加密算法

	private String mode; // 加密方式

	private String padding; // 填充方式

	private int keysize;

	private String provider = "SUN";

	protected static Log LOG = LogFactory.getLog(Asymmetric.class);

	public Asymmetric(String keyalg, String algorithm, int keysize) {
		this(keyalg, algorithm, "", "", keysize);
	}

	public Asymmetric(String keyalg, String algorithm, String mode, String padding, int keysize) {
		this.keyalg = keyalg;
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
		this.keysize = keysize;
		Security.addProvider(new BouncyCastleProvider());
		provider = "BC";
	}

	/**
	 * 生成密钥对，用于非对称加密
	 * 
	 * @throws Exception
	 */
	public KeyPair generateKeyPair() throws Exception {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyalg, provider);
			keyGen.initialize(keysize);
			KeyPair key = keyGen.generateKeyPair();
			return key;
		} catch (Exception e) {
			throw new Exception("没有这种加密算法");
		}
	}

	/**
	 * 根据密钥对明文进行加密，返回密文
	 * 
	 * @param plaintext 明文
	 * @param key 密钥
	 */
	public byte[] encrypt(byte[] plaintext, Key key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(formatAlgorithm(), provider);

			// 用密钥初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// 执行加密操作
			byte[] cryptotext = null;
			// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127 byte第二个为1个byte
			int blockSize = cipher.getBlockSize();
			// 获得加密块加密后块大小
			int outputSize = cipher.getOutputSize(plaintext.length);
			int leavedSize = plaintext.length % blockSize;
			int blocksSize = leavedSize != 0 ? plaintext.length / blockSize + 1 : plaintext.length / blockSize;
			cryptotext = new byte[outputSize * blocksSize];
			int i = 0;
			while (plaintext.length - i * blockSize > 0) {
				if (plaintext.length - i * blockSize > blockSize)
					cipher.doFinal(plaintext, i * blockSize, blockSize, cryptotext, i * outputSize);
				else
					cipher.doFinal(plaintext, i * blockSize, plaintext.length - i * blockSize, cryptotext, i * outputSize);
				i++;
			}
			return cryptotext;
		} catch (InvalidKeyException e) {
			throw new Exception("密钥非法");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("没有这种加密算法");
		} catch (BadPaddingException e) {
			throw new Exception("加密失败");
		}
	}

	/**
	 * 根据密钥对密文进行加密，返回明文
	 * 
	 * @param cryptotext 密文
	 * @param key 密钥
	 */
	public byte[] decrypt(byte[] cryptotext, Key key) throws Exception {
		try {
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(formatAlgorithm(), provider);

			// 用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key);

			// 执行解密操作
			byte[] plaintext = null;

			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;

			while (cryptotext.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(cryptotext, j * blockSize, blockSize));
				j++;
			}
			plaintext = bout.toByteArray();

			return plaintext;
		} catch (InvalidKeyException e) {
			throw new Exception("密钥非法");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("没有这种解密算法");
		} catch (BadPaddingException e) {
			throw new Exception("解密失败");
		}
	}

	/**
	 * 加密字串，字串用UTF-8生成字节
	 * 
	 * @param plaintext 明文
	 * @param key 密钥
	 * @throws Exception
	 */
	public String encrypt(String plaintext, Key key) throws Exception {
		byte[] cryptotext = encrypt(plaintext.getBytes("UTF-8"), key);
		return Base64.encode(cryptotext);
	}

	/**
	 * 解密字串，用UTF-8生成字串
	 * 
	 * @param cryptotext 密文
	 * @param key 密钥
	 * @throws Exception
	 */
	public String decrypt(String cryptotext, Key key) throws Exception {
		byte[] plaintext = decrypt(Base64.decode(cryptotext), key);
		return new String(plaintext, "UTF-8");
	}

	private String formatAlgorithm() {
		// Cipher对象实际完成加密操作
		String str = algorithm;
		if (null != mode && !"".equals(mode)) {
			str += "/" + mode;
		}
		if (null != padding && !"".equals(padding)) {
			str += "/" + padding;
		}
		return str;
	}

	public String getProvider() {
		return provider;
	}
}
