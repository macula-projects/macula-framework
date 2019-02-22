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
package org.macula.boot.web.mvc.annotation.support;

import org.macula.boot.vo.Response;
import org.macula.boot.web.utils.HttpRequestUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p> <b>ExceptionResultReturnValueHandler</b> 根据原始抛出异常的方法是否含有ResponseBody注解来决定异常返回的内容格式 </p>
 *
 * @author Rain
 * @version $Id: ExceptionResultReturnValueHandler.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-6-10
 */
public class ExceptionResultReturnValueHandler extends RequestResponseBodyMethodProcessor {

    /**
     * @param messageConverters
     */
    public ExceptionResultReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Response.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
        if (HttpRequestUtils.isAjaxOrOpenAPIRequest(webRequest.getNativeRequest(HttpServletRequest.class))) {
            mavContainer.setRequestHandled(true);
            if (returnValue != null) {
                super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            }
        } else {
            mavContainer.setRequestHandled(false);
            mavContainer.addAttribute("errors", returnValue);
            mavContainer.setViewName("/error");
        }
    }
}
