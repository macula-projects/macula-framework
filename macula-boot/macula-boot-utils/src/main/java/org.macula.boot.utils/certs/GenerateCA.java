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

import java.io.FileOutputStream;
import java.io.IOException;
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
 * <b>GenerateCA</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 4509 $ $Date: 2013-10-23 18:08:48 +0800 (三, 23 10 2013) $
 */
public class GenerateCA {
	public GenerateCA() {
	}

	public static void main(String[] args) {
		GenerateCA ca = new GenerateCA();
		FileOutputStream caCertOut = null;
		FileOutputStream caPfxOut = null;
		try {
			caCertOut = new FileOutputStream("d:\\ca.cer");
			caPfxOut = new FileOutputStream("d:\\ca.pfx");
			ca.createCA(caCertOut, caPfxOut, CertConfig.CA_STORE_PASS, CertConfig.CA_OU, CertConfig.CA_CN,
					CertConfig.CA_EMAIL, CertConfig.CA_LIMIT_DAY);
			System.out.println("产生CA根证书成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != caCertOut) {
					caCertOut.close();
				}
				if (null != caPfxOut) {
					caPfxOut.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void createCA(OutputStream caCertOut, OutputStream caPfxOut, String password, String OU, String CN,
			String email, int limitday) throws Exception {
		try {
			Security.addProvider(new BouncyCastleProvider());

			String caIssue = CertConfig.BASIC_ISSUE + "OU=" + OU + ",CN=" + CN;// + ",EMAILADDRESS=" + email;
			caIssue = X509Subject.decodeX509Subject(caIssue);

			// 产生CA钥匙对
			KeyPair caKeyPair = CertUtils.generateKeyPair(CertConfig.KEY_ALG, CertConfig.KEY_SIZE);
			PrivateKey caPrivKey = caKeyPair.getPrivate(); // 产生CA私钥
			PublicKey caPubKey = caKeyPair.getPublic(); // 产生CA公钥

			// 产生CA证书
			X509Certificate caCert = CertUtils.createCert(caPubKey, caPrivKey, caIssue, limitday, CertConfig.SIGN_ALG);

			caCertOut.write(caCert.getEncoded());
			caCertOut.close();

			// 初始化CA证书链
			Certificate[] caCadPfx = new Certificate[2];
			caCadPfx[1] = (Certificate) caCert;
			caCadPfx[0] = (Certificate) caCert;

			// 初始化内存密钥库P12格式
			KeyStore caStore = KeyStore.getInstance("PKCS12", "BC");
			caStore.load(null, null);

			caStore.setCertificateEntry(OU + "-caroot", caCert);
			caStore.setKeyEntry(CN, caPrivKey, password.toCharArray(), caCadPfx);
			caStore.store(caPfxOut, password.toCharArray());
		} catch (Exception e) {
			throw new Exception("创建CA证书失败", e);
		}
	}
}
