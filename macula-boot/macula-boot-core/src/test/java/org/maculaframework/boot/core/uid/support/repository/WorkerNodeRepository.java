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

package org.maculaframework.boot.core.uid.support.repository;

import org.maculaframework.boot.core.uid.support.domain.WorkerNode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 * <b>WorkerNodeRepository</b> WorkerNode DAO类
 * </p>
 *
 * @author Rain
 * @since 2019-03-05
 */
public interface WorkerNodeRepository extends JpaRepository<WorkerNode, Long> {

}
