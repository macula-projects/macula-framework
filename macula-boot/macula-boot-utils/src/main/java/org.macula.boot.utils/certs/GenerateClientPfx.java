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
 * <b>GenerateClientPfx</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class GenerateClientPfx {

	public static void main(String[] args) {
		GenerateClientPfx clientCertPfx = new GenerateClientPfx();
		OutputStream userPfxOut = null;
		InputStream caPfxIn = null;
		try {
			userPfxOut = new FileOutputStream("d:\\Server\\IBM\\test.pfx");
			caPfxIn = new FileInputStream("d:\\Server\\IBM\\ca.pfx");
			clientCertPfx.createClientPfx(caPfxIn, CertConfig.CA_STORE_PASS, userPfxOut, CertConfig.CLIENT_PASS,
					CertConfig.CLIENT_OU, CertConfig.CLIENT_CN, CertConfig.CLIENT_EMAIL, CertConfig.CLIENT_LIMIT_DAY);
			System.out.println("产生客户端证书成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != userPfxOut) {
					userPfxOut.close();
				}
				if (null != caPfxIn) {
					caPfxIn.close();
				}
			} catch (IOException e) {
			}
		}

	}

	/**
	 * 创建个人证书
	 * 
	 * @param caPfxIn CA证书流
	 * @param capass CA证书密码
	 * @param userPfxOut 个人证书输出流
	 * @param userpass 个人证书密码
	 * @param OU OU
	 * @param CN CN
	 * @param email 电子邮件
	 * @param limitday 有效天数
	 * @throws Exception
	 */
	public void createClientPfx(InputStream caPfxIn, String capass, OutputStream userPfxOut, String userpass, String OU,
			String CN, String email, int limitday) throws Exception {
		try {
			// 修改安全通道设置
			Security.addProvider(new BouncyCastleProvider());

			// 产生序列号
			java.util.Random usercertserial = new java.util.Random();
			int serial = Math.abs(usercertserial.nextInt());

			// 用户DN
			String userIssue = CertConfig.BASIC_ISSUE + "OU=" + OU + ",CN=" + CN;// + ",EmailAddress=" + email;
			userIssue = X509Subject.decodeX509Subject(userIssue);

			PfxLoadUtils pfxLoad = new PfxLoadUtils(caPfxIn, capass);
			// 获取CA私钥
			PrivateKey caPrivKey = pfxLoad.getPrivateKey();
			// 获取CA证书
			X509Certificate caCert = pfxLoad.getCertificate();
			// 获取CA公钥
			PublicKey caPubKey = caCert.getPublicKey();
			// 获取CA DN
			String caIssue = caCert.getSubjectDN().toString();
			caIssue = X509Subject.decodeX509Subject(caIssue);

			// 用RSA算法产生用户密钥对
			KeyPair userKeyPair = CertUtils.generateKeyPair(CertConfig.KEY_ALG, CertConfig.KEY_SIZE);
			PrivateKey userPrivKey = userKeyPair.getPrivate();
			PublicKey userPubKey = userKeyPair.getPublic();

			// 产生客户端证书
			X509Certificate userCert = CertUtils.createCert(userPubKey, caPrivKey, caPubKey, caIssue, userIssue, serial,
					limitday, CertConfig.SIGN_ALG);

			// 产生pfx格式CA签名证书
			Certificate[] userCadPfx = new Certificate[2];
			userCadPfx[1] = caCert;
			userCadPfx[0] = userCert;
			KeyStore userStore = KeyStore.getInstance("PKCS12", "BC");
			userStore.load(null, null);
			userStore.setKeyEntry(CN, userPrivKey, userpass.toCharArray(), userCadPfx);
			userStore.store(userPfxOut, userpass.toCharArray());
		} catch (Exception e) {
			throw new Exception("创建个人数字证书失败", e);
		}
	}
}
