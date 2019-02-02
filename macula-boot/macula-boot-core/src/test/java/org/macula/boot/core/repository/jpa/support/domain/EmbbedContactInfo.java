/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * EmbbedContactInfo.java 2014年9月1日
 */
package org.macula.boot.core.repository.jpa.support.domain;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * <p>
 * <b>EmbbedContactInfo</b> is
 * </p>
 *
 * @since 2014年9月1日
 * @author Rain
 * @version $Id: EmbbedContactInfo.java 5354 2014-09-01 03:21:07Z wzp $
 */

@Data
@Embeddable
public class EmbbedContactInfo {
	
	private String homeTel;
	
	private String mobile;
	
	private String officeTel;
}
