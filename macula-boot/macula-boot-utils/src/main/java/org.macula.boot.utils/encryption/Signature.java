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

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * <b>Symmetric</b> 签名基类
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class Signature {

	private String keyalg;    // 生成密钥的算法
	
	private String algorithm; // 加密算法

	private String mode; // 加密方式

	private String padding; // 填充方式

	private int keysize;

	private String provider = "SUN";

	protected static Log LOG = LogFactory.getLog(Signature.class);

	public Signature(String keyalg, String algorithm, int keysize) {
		this(keyalg, algorithm, "", "", keysize);
	}

	public Signature(String keyalg, String algorithm, String mode, String padding, int keysize) {
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
	 */
	public KeyPair generateKeyPair() throws Exception {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyalg, provider);
			keyGen.initialize(keysize);
			KeyPair key = keyGen.generateKeyPair();
			return key;
		} catch (Exception e) {
			throw new Exception("没有这种签名算法");
		}
	}

	/**
	 * 签名数据
	 * 
	 * @param plaintext 明文
	 * @param privateKey 私钥
	 */
	public byte[] sign(byte[] plaintext, PrivateKey privateKey) throws Exception {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(formatAlgorithm(), provider);
			// 用密钥初始化Signature对象
			signature.initSign(privateKey);
			signature.update(plaintext);
			// 执行签名操作
			byte[] signed = signature.sign();
			return signed;
		} catch (InvalidKeyException e) {
			throw new Exception("密钥非法");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("没有这种签名算法");
		} catch (SignatureException e) {
			throw new Exception("签名失败");
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param plaintext 明文
	 * @param signed 签名数据
	 * @param publicKey 公钥
	 */
	public boolean verify(byte[] plaintext, byte[] signed, PublicKey publicKey) throws Exception {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(formatAlgorithm(), provider);
			signature.initVerify(publicKey);
			signature.update(plaintext);
			return signature.verify(signed);
		} catch (InvalidKeyException e) {
			throw new Exception("密钥非法");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("没有这种签名算法");
		} catch (SignatureException e) {
			throw new Exception("验证签名失败");
		}
	}

	/**
	 * 签名字串用UTF-8生成字节
	 * 
	 * @param plaintext 明文
	 * @param privateKey 私钥
	 */
	public String sign(String plaintext, PrivateKey privateKey) throws Exception {
		byte[] signed = sign(plaintext.getBytes("UTF-8"), privateKey);
		return Base64.encode(signed);
	}

	/**
	 * 验证签名，用UTF-8生成字串
	 * 
	 * @param plaintext 明文
	 * @param signed 签名数据
	 * @param publicKey 公钥
	 */
	public boolean verify(String plaintext, String signed, PublicKey publicKey) throws Exception {
		return verify(plaintext.getBytes("UTF-8"), Base64.decode(signed), publicKey);
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
