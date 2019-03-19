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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * <b>MD5</b> is a class which encryption the string by MD5
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5584 $ $Date: 2015-05-18 15:54:35 +0800 (Mon, 18 May 2015) $
 */
public class MD5 {
	private static Digest digest;

	protected static Log LOG = LogFactory.getLog(MD5.class);

	static {
		digest = new Digest("MD5");
	}

	/**
	 * 生成消息摘要
	 * @param bytes
	 */
	public static String gen(byte[] bytes) {
		try {
			return digest.gen(bytes);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static String gen(String str) {
		try {
			return digest.gen(str);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static void main(String[] args) {
		String str = "test";
		System.out.println("消息:" + str);
		String digest = gen(str);
		System.out.println("摘要:" + digest);
	}
}
