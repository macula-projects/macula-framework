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
package org.macula.boot.core.utils;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p> <b>EnvironmentUtils</b> 是环境变量助手类. </p>
 * 
 * @since 2010-12-28
 * @author Wilson Luo
 * @version $Id: EnvironmentUtils.java 3807 2012-11-21 07:31:51Z wilson $
 */
public final class EnvironmentUtils {
	private EnvironmentUtils() {
		//Noop
	}

	private static long TIME_DIFF = 0;

	/**
	 * 获取当前时间.
	 */
	public static TemporalAccessor getCurrentTime() {
		return Instant.ofEpochMilli(System.currentTimeMillis() + TIME_DIFF);
	}

	/**
	 * TODO 有什么用待定
	 * @param mids
	 * @return
	 */
	public static Collection<Long> toIdCollection(String mids) {
		Collection<Long> idCollection = new ArrayList<Long>();
		if (mids != null && mids.length() > 0) {
			String[] ids = mids.split(",");
			if (ids != null && ids.length > 0) {
				for (String id : ids) {
					try {
						idCollection.add(Long.parseLong(id));
					} catch (NumberFormatException e) {
						//ignore;
					}
				}
			}
		}
		return idCollection;
	}

	public static final void setTimeDiff(long diff) {
		TIME_DIFF = diff;
	}

}
