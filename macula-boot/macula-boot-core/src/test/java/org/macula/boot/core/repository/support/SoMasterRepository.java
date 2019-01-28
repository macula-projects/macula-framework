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
 * SoMasterRepository.java 2016年11月10日
 */
package org.macula.boot.core.repository.support;

import org.macula.boot.core.repository.MaculaJpaRepository;
import org.macula.boot.core.repository.support.domain.SoMaster;

/**
 * <p>
 * <b>SoMasterRepository</b> is
 * </p>
 *
 * @author Rain
 * @version $Id$
 * @since 2016年11月10日
 */
public interface SoMasterRepository extends MaculaJpaRepository<SoMaster, Long> {
    public SoMaster findBySoNo(String soNo);
}
