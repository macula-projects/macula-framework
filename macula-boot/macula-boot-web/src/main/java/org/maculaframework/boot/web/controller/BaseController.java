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
package org.maculaframework.boot.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.maculaframework.boot.core.exception.MaculaArgumentException;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.vo.Response;
import org.maculaframework.boot.web.mvc.annotation.support.FormBeanArgumentResolver;
import org.maculaframework.boot.web.utils.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> <b>BaseController</b> 所有Controller的基类，提供基础的异常处理功能 </p>
 *
 * @author Rain
 * @version $Id: BaseController.java 5770 2015-08-24 02:23:52Z wzp $
 * @since 2011-1-7
 */
@SuppressWarnings("unchecked")
public abstract class BaseController {

    private static final Map<Class<?>, String> controllerPathMapping = new ConcurrentHashMap<>();
    @Autowired
    private ObjectMapper mapper;

    /**
     * 判断绑定过程中是否出现错误
     *
     * @param results
     */
    protected boolean hasErrors(BindingResult... results) {
        BindingResult[] bindingResults = getMergedBindingResults(results);
        if (bindingResults != null) {
            for (BindingResult bindingResult : bindingResults) {
                if (bindingResult.hasErrors()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 提取{@link FormBeanArgumentResolver}中"BINDING_RESULT_LIST_NAME"指定的BindingResult
     * 合并到results中
     *
     * @param results BindingResult
     */
    protected BindingResult[] getMergedBindingResults(BindingResult... results) {
        // 从Request中提取BindingResult
        HttpServletRequest request = HttpRequestUtils.getRequest();
        List<BindingResult> bindingResults = null;
        if (request != null) {
            bindingResults = (List<BindingResult>) request
                    .getAttribute(FormBeanArgumentResolver.BINDING_RESULT_LIST_NAME);
        }
        if (results != null) {
            if (bindingResults == null) {
                bindingResults = new ArrayList<BindingResult>();
            }
            for (BindingResult bindingResult : results) {
                if (!bindingResults.contains(bindingResult)) {
                    bindingResults.add(bindingResult);
                }
            }
        }
        return bindingResults.toArray(new BindingResult[bindingResults.size()]);
    }

    /**
     * 处理Controller的异常
     */
    @ExceptionHandler(MaculaException.class)
    public Response handlerCoreException(MaculaException ex, HttpServletRequest req) {
        return new Response(ex);
    }

    /**
     * 处理输入参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Response hangdlerFormBindException(IllegalArgumentException ex, HttpServletRequest req) {
        return new Response(new MaculaArgumentException(ex));
    }

    /**
     * 相对于Controller中的RequestMapping所指定的路径
     *
     * @param path URL路径
     */
    protected String getRelativePath(String path) {
        Class<?> clz = getClass();
        String controllerPath = controllerPathMapping.get(clz);
        if (controllerPath == null) {
            Class<?> top = clz;
            RequestMapping mappings = null;
            while (top != Object.class) {
                mappings = top.getAnnotation(RequestMapping.class);
                if (mappings != null) {
                    break;
                }
                top = top.getSuperclass();
            }
            if (mappings != null) {
                controllerPath = mappings.value()[0];
            } else {
                controllerPath = StringUtils.EMPTY;
            }
            controllerPathMapping.put(clz, controllerPath);
        }
        return controllerPath + path;
    }

    /**
     * 将对象转为JSON格式的数据
     *
     * @param value
     * @return String
     */
    protected String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
        }
        return null;
    }
}
