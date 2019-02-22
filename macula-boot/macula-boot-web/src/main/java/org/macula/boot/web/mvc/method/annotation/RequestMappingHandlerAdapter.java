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

import org.macula.boot.web.mvc.annotation.TokenValidation;
import org.macula.boot.web.mvc.annotation.support.FormBeanArgumentResolver;
import org.macula.boot.web.mvc.annotation.support.OpenApiReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> <b>RequestMappingHandlerAdapter</b> 是RequestMappingHandlerAdapter的扩展 </p>
 *
 * @author zhengping_wang
 * @author Wilson Luo
 * @version $Id: RequestMappingHandlerAdapter.java 5252 2014-07-09 09:36:50Z wilson $
 * @since 2011-10-18
 */
public class RequestMappingHandlerAdapter extends
        org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter {

    @Autowired(required = false)
    private TokenValidation tokenValidation;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        // 将OpenApiReturnValueHandler插入到RequestResponseBodyMethodProcessor后面
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
        returnValueHandlers.addAll(this.getReturnValueHandlers());

        for (int i = 0; i < returnValueHandlers.size(); i++) {
            HandlerMethodReturnValueHandler handler = returnValueHandlers.get(i);
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                returnValueHandlers.add(i + 1, new OpenApiReturnValueHandler(getMessageConverters()));
                break;
            }
        }
        this.setReturnValueHandlers(returnValueHandlers);

        // 将FormBean和Pageable插入到ServletModelAttributeMethodProcessor后面
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
        argumentResolvers.addAll(this.getArgumentResolvers());

        for (int i = 0; i < argumentResolvers.size(); i++) {
            HandlerMethodArgumentResolver resolver = argumentResolvers.get(i);
            if (resolver instanceof ServletModelAttributeMethodProcessor) {
                FormBeanArgumentResolver formBeanResolver = new FormBeanArgumentResolver(getWebBindingInitializer());
                formBeanResolver.setTokenValidation(tokenValidation);
                // 使用bean.field方式注入对象
                argumentResolvers.add(i + 1, formBeanResolver);
                // 自动绑定分页设置
                PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
                pageableArgumentResolver.setSizeParameterName("rows");  // 保持兼容
                pageableArgumentResolver.setOneIndexedParameters(true); // 请求参数中page=1表示第一页
                // 排序是sort=field1,field2,asc&sort=field3,field4,desc
                argumentResolvers.add(i + 2, pageableArgumentResolver);
                break;
            }
        }
        this.setArgumentResolvers(argumentResolvers);
    }

    /**
     * @param tokenValidation the tokenValidation to set
     */
    public void setTokenValidation(TokenValidation tokenValidation) {
        this.tokenValidation = tokenValidation;
    }
}
