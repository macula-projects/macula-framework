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
package org.macula.boot.core.repository.hibernate.audit;

import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * <b>AuditChangedEvent</b> 数据变化的审计事件
 * </p>
 *
 * @since 2011-3-5
 * @author Rain
 * @version $Id: AuditDeletedEvent.java 4731 2013-12-19 07:38:12Z wilson $
 */
public class AuditDeletedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * @param source
	 */
	public AuditDeletedEvent(AuditDeleted source) {
		super(source);
	}

	@Override
	public AuditDeleted getSource() {
		return (AuditDeleted) super.getSource();
	}
}
