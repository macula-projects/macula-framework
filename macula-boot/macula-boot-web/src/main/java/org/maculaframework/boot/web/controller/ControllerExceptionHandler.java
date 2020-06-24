/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.web.controller;

import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.exception.MaculaArgumentException;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;
import org.maculaframework.boot.vo.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * <b>ControllerExceptionHandler</b> 控制器异常全局处理器
 * </p>
 *
 * @author Rain
 * @since 2020-06-24
 */

@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * 处理Controller的异常
     */
    @ExceptionHandler(Exception.class)
    public Response handlerCoreException(Exception ex, HttpServletRequest req) {

        if (ex instanceof MaculaException) {
            return new Response((MaculaException)ex);
        }

        if (ex instanceof IllegalArgumentException) {
            return new Response(new MaculaArgumentException((IllegalArgumentException)ex));
        }


        if (ex.getClass().equals("org.apache.dubbo.rpc.RpcException")) {
            return new Response(new ServiceException(MaculaConstants.EXCEPTION_CODE_RPC, "org.apache.dubbo.rpc.RpcException", ex));
        }

        ServiceException sex = new ServiceException(MaculaConstants.EXCEPTION_CODE_UNKNOWN, "org.maculaframework.boot.core.exception.ServiceException", ex);
        return new Response(sex);
    }
}
