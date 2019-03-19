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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * <p>
 * <b>CertUtils</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class CertUtils {

	/**
	 * 创建数字证书
	 * @param pubKey 公钥
	 * @param caPrivKey CA私钥
	 * @param caPubKey CA公钥
	 * @param caIssue CA的DN
	 * @param userIssue 用户的DN
	 * @param serial 序列号
	 * @param limitday 有效天数
	 * @param signAlgoritm 签名算法
	 * @return X509Certificate
	 */
	public static X509Certificate createCert(PublicKey pubKey, PrivateKey caPrivKey, PublicKey caPubKey,
			String caIssue, String userIssue, long serial, int limitday, String signAlgoritm) throws Exception {

		Date notBefore = new Date(System.currentTimeMillis());
		Date notAfter = new Date(System.currentTimeMillis() + limitday * (1000L * 60 * 60 * 24));
		X500Name issueDn = new X500Name(caIssue);
		X500Name subjectDn = new X500Name(userIssue);
		BigInteger sn = BigInteger.valueOf(serial);

		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issueDn, sn, notBefore, notAfter, subjectDn,
				pubKey);

		ContentSigner sigGen = new JcaContentSignerBuilder(signAlgoritm).setProvider("BC").build(caPrivKey);
		X509CertificateHolder holder = builder.build(sigGen);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is1 = new ByteArrayInputStream(holder.toASN1Structure().getEncoded());
		X509Certificate cert = (X509Certificate) cf.generateCertificate(is1);
		is1.close();

		cert.checkValidity(new Date());

		cert.verify(caPubKey);

		return cert;
	}

	/**
	 * 创建自签名证书
	 * 
	 * @param pubKey 公钥
	 * @param privKey 私钥
	 * @param issue DN
	 * @param limitday 有效天数
	 * @param signAlgoritm 签名算法
	 */
	public static X509Certificate createCert(PublicKey pubKey, PrivateKey privKey, String issue, int limitday,
			String signAlgoritm) throws Exception {
		return createCert(pubKey, privKey, pubKey, issue, issue, 1L, limitday, signAlgoritm);
	}

	/**
	 * 生成公钥私钥对
	 * 
	 * @param keyalg KEY算法
	 * @param keysize KEY长度
	 */
	public static KeyPair generateKeyPair(String keyalg, int keysize) throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyalg);
		keyGen.initialize(keysize);
		KeyPair key = keyGen.generateKeyPair();
		return key;
	}
}
