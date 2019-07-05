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
package org.maculaframework.boot.web.mvc.annotation.support;

import org.maculaframework.boot.vo.ExecuteResponse;
import org.maculaframework.boot.vo.PageResponse;
import org.maculaframework.boot.vo.Response;
import org.maculaframework.boot.web.mvc.annotation.OpenApi;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p> <b>OpenApiReturnValueHandler</b> 处理含有{@link OpenApi}注解的方法的返回值 如果返回值不是 {@link Response}类型、{@link Map}类型，则会转换为
 * {@link Response}类型 返回，并且根据客户端的需要返回JSON格式或者XML格式 </p>
 *
 * @author zhengping_wang
 * @version $Id: OpenApiReturnValueHandler.java 5739 2015-08-18 02:13:41Z wzp $
 * @since 2011-7-8
 */
public class OpenApiReturnValueHandler extends RequestResponseBodyMethodProcessor {

    /**
     * @param messageConverters
     */
    public OpenApiReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(OpenApi.class) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
        mavContainer.setRequestHandled(true);
        Object newValue = returnValue;
        Class<?> returnParaType = returnType.getParameterType();
        if (!void.class.isAssignableFrom(returnParaType)) {
            // 不是Response、Map、Model等类型的返回值，需要包裹为Response类型
            if (!Response.class.isAssignableFrom(returnParaType) && !Map.class.isAssignableFrom(returnParaType)
                    && !Model.class.isAssignableFrom(returnParaType)) {

                if (Page.class.isAssignableFrom(returnType.getParameterType())) {
                    newValue = new PageResponse<Object>((Page<Object>) returnValue);
                } else {
                    newValue = new ExecuteResponse<Object>(returnValue);
                }
            }

            if (newValue == null) {
                newValue = new ExecuteResponse<Object>(returnValue);
            }

            writeWithMessageConverters(newValue, returnType, webRequest);
        }
    }
}
