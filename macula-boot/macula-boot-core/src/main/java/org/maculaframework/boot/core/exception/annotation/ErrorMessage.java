/**
 * ErrorMessage.java 2015年9月24日
 */
package org.maculaframework.boot.core.exception.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * <b>ErrorMessage</b> Service方法上面默认错误信息的注解
 * </p>
 *
 * @since 2015年9月24日
 * @author Rain
 * @version $Id: ErrorMessage.java 5874 2015-09-24 11:22:28Z wzp $
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ErrorMessage {
	String value() default "";
}
