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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * <p>
 * <b>CheckCertSign</b> is a class which ...
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 4509 $ $Date: 2013-10-23 18:08:48 +0800 (三, 23 10 2013) $
 */
public class CheckCertSign {
	public CheckCertSign() {
	}

	public static void main(String[] args) {
		CheckCertSign checkCertSign = new CheckCertSign();
		InputStream caCertIn = null;
		InputStream userPfxIn = null;
		try {
			caCertIn = new FileInputStream("d:\\ca.cer");
			userPfxIn = new FileInputStream("d:\\test.pfx");
			boolean result = checkCertSign.checkPfxSign(caCertIn, userPfxIn, "password");
			System.out.println("验证结果:" + result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != caCertIn) {
					caCertIn.close();
				}
				if (null != userPfxIn) {
					userPfxIn.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public boolean checkSign(InputStream caCertIn, InputStream userCertIn) {
		try {
			// CA 的证书
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			java.security.cert.Certificate cac = cf.generateCertificate(caCertIn);

			// 用户的签名证书
			java.security.cert.Certificate clientc = cf.generateCertificate(userCertIn);

			PublicKey pbk = cac.getPublicKey();
			boolean pass = false;
			try {
				clientc.verify(pbk);
				pass = true;
			} catch (Exception e) {
				pass = false;
			}
			return pass;
		} catch (Exception ex) {
			return false;
		}

	}

	public boolean checkPfxSign(InputStream caCertIn, InputStream userPfxIn, String password) {
		try {		
			// CA 的证书
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			java.security.cert.Certificate cac = cf.generateCertificate(caCertIn);

			PfxLoadUtils pfxLoad = new PfxLoadUtils(userPfxIn, password);
			X509Certificate userCert = pfxLoad.getCertificate();		

			PublicKey pbk = cac.getPublicKey();
			boolean pass = false;
			try {
				userCert.verify(pbk);
				pass = true;
			} catch (Exception e) {
				pass = false;
			}
			return pass;
		} catch (Exception ex) {
			return false;
		}
	}
}
