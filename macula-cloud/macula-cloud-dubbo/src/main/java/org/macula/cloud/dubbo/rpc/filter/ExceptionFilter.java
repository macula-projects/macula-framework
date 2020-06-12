/**
 * ExceptionFilter.java 2015年11月5日
 */
package org.macula.cloud.dubbo.rpc.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;
import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;
import org.maculaframework.boot.core.exception.translator.MaculaExceptionTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * <b>ExceptionFilter</b> 对默认的ExceptionFilter扩展，处理MaculaException，让这个异常也直接抛出
 * </p>
 *
 * @since 2015年11月5日
 * @author Rain
 * @version $Id: ExceptionFilter.java 5937 2015-11-05 07:06:29Z wzp $
 */
@Activate(group = CommonConstants.PROVIDER)
public class ExceptionFilter extends org.apache.dubbo.rpc.filter.ExceptionFilter {
    private final Logger logger;
    
	
	@Autowired(required = false)
	private List<MaculaExceptionTranslator> exceptionTranslators;
    
    public ExceptionFilter() {
        this(LoggerFactory.getLogger(ExceptionFilter.class));
    }
    
    public ExceptionFilter(Logger logger) {
        this.logger = logger;
    }

    @Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = result.getException();
                    
                    //将Hibernate异常，JPA异常进行翻译
                    if (!(exception instanceof MaculaException)) {
                		if (exceptionTranslators != null) {
                			for (MaculaExceptionTranslator translator : exceptionTranslators) {
                				MaculaException coreException = translator.translateExceptionIfPossible(exception);
                				if (coreException != null) {
                					exception = coreException;
                				}
                			}
                		}
                        
                    }

                    // 如果是checked异常，直接抛出
                    if (! (exception instanceof RuntimeException) && (exception instanceof Exception)) {
                        return result;
                    }
                    // 在方法签名上有声明，直接抛出
                    try {
                        Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?>[] exceptionClassses = method.getExceptionTypes();
                        for (Class<?> exceptionClass : exceptionClassses) {
                            if (exception.getClass().equals(exceptionClass)) {
                                return result;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        return result;
                    }

                    // 未在方法签名上定义的异常，在服务器端打印ERROR日志
                    logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);

                    // 异常类和接口类在同一jar包里，直接抛出
                    String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                    String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                    if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)){
                        return result;
                    }
                    // 是JDK自带的异常，直接抛出
                    String className = exception.getClass().getName();
                    if (className.startsWith("java.") || className.startsWith("javax.")) {
                        return result;
                    }
                    // 是Dubbo本身的异常，直接抛出
                    if (exception instanceof RpcException) {
                        return result;
                    }
                    
                    if (exception instanceof MaculaException) {
                    	exception = new ServiceException(MaculaConstants.EXCEPTION_CODE_RPC, exception.getMessage(), new RuntimeException(StringUtils.toString(exception)));
                    	return new AppResponse(exception);
                    }


                    // 否则，包装成RuntimeException抛给客户端
                    return new AppResponse(new RuntimeException(StringUtils.toString(exception)));
                } catch (Throwable e) {
                    logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
                    return result;
                }
            }
            return result;
        } catch (RuntimeException e) {
            logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                    + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
	}
	
}
