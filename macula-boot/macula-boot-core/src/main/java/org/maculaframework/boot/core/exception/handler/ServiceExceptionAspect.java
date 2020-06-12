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
 * ServiceExceptionAspect.java 2015年9月24日
 */
package org.maculaframework.boot.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;
import org.maculaframework.boot.core.exception.annotation.ErrorMessage;
import org.maculaframework.boot.core.exception.translator.MaculaExceptionTranslator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b>ServiceExceptionAspect</b> 服务层统一异常处理
 * </p>
 *
 * @author Rain
 * @version $Id: ServiceExceptionAspect.java 5939 2015-11-05 11:37:05Z wzp $
 * @since 2015年9月24日
 */
@Slf4j
@Aspect
public class ServiceExceptionAspect implements BeanFactoryAware {

    private List<MaculaExceptionTranslator> exceptionTranslators;

    @Pointcut("@annotation(org.springframework.stereotype.Service)")
    public void service() {

    }

    @AfterThrowing(pointcut = "service()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        if (!(ex instanceof MaculaException)) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            try {
                method = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (Exception e) {
            }

            ErrorMessage errorMessage = method.getAnnotation(ErrorMessage.class);

            String message = errorMessage == null ? "org.macula.boot.core.exception.ServiceException" : errorMessage.value();

            log.error(message, ex);

            throw translate(message, ex);
        }
    }

    private MaculaException translate(String message, Throwable ex) {
        if (exceptionTranslators != null) {
            for (MaculaExceptionTranslator translator : exceptionTranslators) {
                MaculaException coreException = translator.translateExceptionIfPossible(ex);
                if (coreException != null) {
                    return new ServiceException(MaculaConstants.EXCEPTION_CODE_DATABASE, message, coreException);
                }
            }
        }

        if (ex.getClass().equals("org.apache.dubbo.rpc.RpcException")) {
            return new ServiceException(MaculaConstants.EXCEPTION_CODE_RPC, message, ex);
        }

        return new ServiceException(MaculaConstants.EXCEPTION_CODE_UNKNOWN, message, ex);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ListableBeanFactory) {
            Map<String, MaculaExceptionTranslator> beans = ((ListableBeanFactory) beanFactory).getBeansOfType(MaculaExceptionTranslator.class);
            exceptionTranslators = new ArrayList<>(beans.values());
        }
    }
}
