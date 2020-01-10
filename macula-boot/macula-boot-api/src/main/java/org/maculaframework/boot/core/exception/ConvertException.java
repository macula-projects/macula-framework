/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * ConvertException.java 2015年10月19日
 */
package org.maculaframework.boot.core.exception;

/**
 * <p>
 * <b>ConvertException</b> 类型转换异常
 * </p>
 *
 * @author Rain
 * @version $Id: ConvertException.java 5906 2015-10-19 09:40:12Z wzp $
 * @since 2015年10月19日
 */
public class ConvertException extends MaculaException {

    private static final long serialVersionUID = 1L;

    public ConvertException(Throwable e) {
        super("Convert Error", e);
    }
}
