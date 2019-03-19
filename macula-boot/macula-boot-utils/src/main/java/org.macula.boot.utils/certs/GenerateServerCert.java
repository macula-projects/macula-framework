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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * <b>GenerateServerCert</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class GenerateServerCert {

	public static void main(String[] args) {
		GenerateServerCert serverCert = new GenerateServerCert();
		OutputStream serverCertOut = null;
		InputStream caPfxIn = null;
		try {
			serverCertOut = new FileOutputStream("d:\\Server\\IBM\\client.jks");
			caPfxIn = new FileInputStream("d:\\Server\\IBM\\ca.pfx");
			serverCert.createServerCert(caPfxIn, CertConfig.CA_STORE_PASS, serverCertOut, CertConfig.SERVER_PASS,
					CertConfig.SERVER_OU, CertConfig.SERVER_CN, CertConfig.SERVER_EMAIL, CertConfig.SERVER_LIMIT_DAY);
			System.out.println("产生服务器证书成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != serverCertOut) {
					serverCertOut.close();
				}
				if (null != caPfxIn) {
					caPfxIn.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 用CA根证书签发服务器端根证书
	 * 
	 * @param caPfxIn CA证书输入流
	 * @param capass CA证书密码
	 * @param serverCertOut 服务器证书输出流
	 * @param serverpass 服务器证书密码
	 * @param OU OU
	 * @param CN CN
	 * @param email 电子邮件
	 * @param limitday 有效天数
	 * @throws Exception
	 */
	public void createServerCert(InputStream caPfxIn, String capass, OutputStream serverCertOut, String serverpass,
			String OU, String CN, String email, int limitday) throws Exception {
		try {
			Security.addProvider(new BouncyCastleProvider());
			int serial = 123456789;

			// 用户DN
			String serverIssue = CertConfig.BASIC_ISSUE + "OU=" + OU + ", CN=" + CN;// + ", EmailAddress=" + email;
			serverIssue = X509Subject.decodeX509Subject(serverIssue);

			PfxLoadUtils pfxLoad = new PfxLoadUtils(caPfxIn, capass);
			// 获取CA私钥
			PrivateKey caPrivKey = pfxLoad.getPrivateKey();
			// 获取CA证书
			X509Certificate caCert = pfxLoad.getCertificate();
			// 获取CA公钥
			PublicKey caPubKey = caCert.getPublicKey();
			// 获取CA公钥
			String caIssue = caCert.getSubjectDN().toString();
			caIssue = X509Subject.decodeX509Subject(caIssue);

			// 生成用户钥匙对
			KeyPair keyPair = CertUtils.generateKeyPair(CertConfig.KEY_ALG, CertConfig.KEY_SIZE);
			PrivateKey userPrivKey = keyPair.getPrivate();
			PublicKey userPubKey = keyPair.getPublic();

			// 产生服务器端证书
			X509Certificate serverCert = CertUtils.createCert(userPubKey, caPrivKey, caPubKey, caIssue, serverIssue, serial,
					limitday, CertConfig.SIGN_ALG);

			// 转换CA证书与用户证书类型为标准格式
			java.security.cert.Certificate cac = (Certificate) caCert;
			java.security.cert.Certificate clientc = (Certificate) serverCert;
			// 形成证书链
			java.security.cert.Certificate[] cchain = { clientc, cac };

			// 保存服务器端签名证书至密钥库
			KeyStore ks = KeyStore.getInstance("JKS");

			ks.load(null, null);
			ks.setKeyEntry("ibmwebspheremqmqgbss", userPrivKey, serverpass.toCharArray(), cchain);
			ks.setCertificateEntry("Infinitus(China) Company Ltd Websphere MQ Root CA", cac);
			ks.store(serverCertOut, serverpass.toCharArray());
		} catch (Exception e) {
			throw new Exception("创建服务器数字证书失败", e);
		}
	}
}
