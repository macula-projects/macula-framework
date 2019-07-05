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
package org.maculaframework.boot.web.exception;

import org.maculaframework.boot.ApplicationContext;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.vo.FieldError;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> <b>FormBindException</b> 绑定出错后用户可以选择抛出该异常 </p>
 *
 * @author zhengping_wang
 * @author Wilson Luo
 * @version $Id: FormBindException.java 4702 2013-11-28 09:32:13Z wilson $
 * @since 2011-7-8
 */
public class FormBindException extends MaculaException {

    private static final long serialVersionUID = 1L;

    private final BindingResult[] bindingResults;

    private final List<FieldError> fieldErrors = new ArrayList<FieldError>();

    private final StringBuilder fullMessage = new StringBuilder();

    public FormBindException(BindingResult... bindingResults) {
        super("form.bind.exception");
        this.bindingResults = bindingResults;
        for (BindingResult result : bindingResults) {
            if (result.hasErrors()) {
                for (ObjectError objectError : result.getAllErrors()) {
                    if (objectError instanceof org.springframework.validation.FieldError) {
                        // 字段级的错误，ObjectName+.+字段名返回错误信息
                        if (!StringUtils.isEmpty(objectError.getObjectName())) {
                            FieldError fieldError = new FieldError(objectError.getObjectName() + "."
                                    + ((org.springframework.validation.FieldError) objectError).getField(),
                                    ApplicationContext.getMessage(objectError));
                            fieldErrors.add(fieldError);
                        } else {
                            FieldError fieldError = new FieldError(
                                    ((org.springframework.validation.FieldError) objectError).getField(),
                                    ApplicationContext.getMessage(objectError));
                            fieldErrors.add(fieldError);
                        }
                    } else {
                        // 全局错误
                        fullMessage.append(ApplicationContext.getMessage(objectError) + "\n");
                    }
                }
            }
        }
    }

    public BindingResult[] getBindingResults() {
        // 这里不克隆，减少内存操作
        return this.bindingResults;
    }

    @Override
    public String getFullStackMessage() {
        return fullMessage.toString();
    }

    /**
     * @return the fieldErrors
     */
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * @return the fullMessage
     */
    public StringBuilder getFullMessage() {
        return fullMessage;
    }
}
