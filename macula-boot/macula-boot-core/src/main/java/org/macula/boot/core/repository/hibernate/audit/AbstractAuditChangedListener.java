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

import org.springframework.context.ApplicationListener;

/**
 * <p>
 * <b>AbstractAuditChangedListener</b> 是数据变化事件的监听器
 * </p>
 *
 * @since 2011-3-5
 * @author Rain
 * @version $Id: AbstractAuditChangedListener.java 3807 2012-11-21 07:31:51Z wilson $
 */
public abstract class AbstractAuditChangedListener implements ApplicationListener<AuditChangedEvent> {

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(AuditChangedEvent auditChangedEvent) {
		onAuditChanged(auditChangedEvent.getSource());
	}
	
	/**
	 * 实现该方法，获取所需的数据变化实体，以便做进一步操作
	 * @param auditChanged
	 */
	protected abstract void onAuditChanged(AuditChanged auditChanged);

}
