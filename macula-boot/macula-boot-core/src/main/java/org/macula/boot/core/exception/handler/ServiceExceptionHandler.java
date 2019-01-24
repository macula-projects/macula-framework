/**
 * ServiceExceptionHandler.java 2015年9月24日
 */
package org.macula.boot.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.macula.boot.MaculaConstants;
import org.macula.boot.core.exception.ServiceException;
import org.macula.boot.core.exception.annotation.ErrorMessage;
import org.macula.boot.core.exception.translator.MaculaExceptionTranslator;
import org.macula.boot.exception.MaculaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * <b>ServiceExceptionHandler</b> 服务层统一异常处理
 * </p>
 *
 * @author Rain
 * @version $Id: ServiceExceptionHandler.java 5939 2015-11-05 11:37:05Z wzp $
 * @since 2015年9月24日
 */
@Slf4j
@Component
public class ServiceExceptionHandler {

    @Autowired(required = false)
    private List<MaculaExceptionTranslator> exceptionTranslators;

    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        if (!(ex instanceof MaculaException)) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            try {
                method = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (Exception e) {
            }

            ErrorMessage errorMessage = method.getAnnotation(ErrorMessage.class);

            String message = errorMessage == null ? "org.macula.core.exception.ServiceException" : errorMessage.value();

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
        return new ServiceException(MaculaConstants.EXCEPTION_CODE_DATABASE, message, ex);
    }
}
