/**
 * Copyright 2010-2012 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.exception.translator;

import org.macula.boot.exception.MaculaException;
import org.springframework.core.Ordered;

/**
 * <p> <b>CoreExceptionTranslator</b> 是异常转化类. </p>
 *
 * @since 2011-7-8
 * @author Wilson Luo
 * @version $Id: MaculaExceptionTranslator.java 5877 2015-09-25 06:50:45Z wzp $
 */
public interface MaculaExceptionTranslator extends Ordered {

    MaculaException translateExceptionIfPossible(Throwable ex);
}
