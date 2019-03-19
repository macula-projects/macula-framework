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

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * <b>PfxLoadUtils</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 4509 $ $Date: 2013-10-23 18:08:48 +0800 (三, 23 10 2013) $
 */
public class PfxLoadUtils {
	private KeyStore keystore;

	public PfxLoadUtils(InputStream in, String password) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		// 读取证书
		keystore = KeyStore.getInstance("PKCS12", "BC");
		keystore.load(in, password.toCharArray());
	}

	public PrivateKey getPrivateKey() throws Exception {
		// 获取私钥
		PrivateKey caPrivKey = (PrivateKey) (keystore.getKey(getStoreAlias(), null));
		return caPrivKey;
	}

	public PublicKey getPublicKey() throws Exception {
		PublicKey caPubKey = getCertificate().getPublicKey();
		return caPubKey;
	}

	public X509Certificate getCertificate() throws Exception {
		// 获取证书
		X509Certificate caCert = (X509Certificate) (keystore.getCertificate(getStoreAlias()));
		return caCert;
	}

	private String getStoreAlias() throws Exception {
		// 获取证书的存储别名
		String alias = null;
		Enumeration<String> en = keystore.aliases();
		while (en.hasMoreElements()) {
			String keyAlias = en.nextElement();
			if (keystore.isKeyEntry(keyAlias)) {
				alias = keyAlias;
			}
		}
		return alias;
	}
}
