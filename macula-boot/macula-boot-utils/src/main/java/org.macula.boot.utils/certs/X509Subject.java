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

/**
 * <p>
 * <b>X509Subject</b> is X509解码
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
import java.util.StringTokenizer;

/**
 * Class X509Subject: Process subjects X509
 * 
 * @since jdk1.3
 * @version 1.5pre
 */

public class X509Subject {

	private static String decodeField(String codigo) {

		if (codigo.charAt(0) == '#') {
			codigo = codigo.substring(5);

			byte b[] = new byte[(codigo.length() / 2)];

			for (int i = 0; i < b.length; i++) {
				Integer tmpByte = Integer.decode("0x" + codigo.substring(0, 2));
				b[i] = tmpByte.byteValue();
				if (codigo.length() >= 2)
					codigo = codigo.substring(2);
			}

			try {
				codigo = new String(b, "UTF-8");
			} catch (java.io.UnsupportedEncodingException e) {
			}
		}
		return codigo;
	}

	/**
	 * Process X509Subject
	 * 
	 * @param x509Subject X509格式的Subject
	 * @return String
	 */
	public static String decodeX509Subject(String x509Subject) {
		String postalcode = null;
		String pais = null;
		String ciudad = null;
		String organizacion = null;
		String departamento = null;
		String nombre = null;
		String email = null;

		StringTokenizer stX509Subject = new StringTokenizer(x509Subject, ",");
		while (stX509Subject.hasMoreTokens()) {
			StringTokenizer stTmp = new StringTokenizer(stX509Subject.nextToken(), "=");
			String nom = stTmp.nextToken().trim();
			String val = stTmp.nextToken().trim();

			if (nom.equals("C"))
				postalcode = val;
			else if (nom.equals("ST"))
				pais = val;
			else if (nom.equals("L"))
				ciudad = val;
			else if (nom.equals("O"))
				organizacion = val;
			else if (nom.equals("OU"))
				departamento = val;
			else if (nom.equals("CN"))
				nombre = val;
			else if (nom.equals("EmailAddress"))
				email = val;
		}

		String newSubject = new String("");

		// Add common name
		if (nombre != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "CN=" + decodeField(nombre);

		// Add departament
		if (departamento != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "OU=" + decodeField(departamento);

		// Add organization
		if (organizacion != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "O=" + decodeField(organizacion);

		// Add city
		if (ciudad != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "L=" + decodeField(ciudad);

		// Add postal code
		if (postalcode != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "C=" + decodeField(postalcode);

		// Add country code
		if (pais != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "ST=" + decodeField(pais);

		// Add email
		if (email != null)
			newSubject = newSubject + (newSubject.equals("") ? "" : ", ") + "EmailAddress=" + decodeField(email);

		return newSubject;
	}
}
