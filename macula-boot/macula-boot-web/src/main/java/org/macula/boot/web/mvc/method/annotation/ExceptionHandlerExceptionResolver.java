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

package org.macula.boot.web.mvc.method.annotation;

import org.macula.boot.web.mvc.annotation.support.ExceptionResultReturnValueHandler;

/**
 * <p>
 * <b>ExceptionHandlerExceptionResolver</b> ExceptionHandlerExceptionResolver扩展
 * </p>
 *
 * @author Rain
 * @since 2019-02-23
 */
public class ExceptionHandlerExceptionResolver extends org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver {

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        ExceptionResultReturnValueHandler resultReturnValueHandler = new ExceptionResultReturnValueHandler(this.getMessageConverters());
        this.getReturnValueHandlers().addHandler(resultReturnValueHandler);
    }
}