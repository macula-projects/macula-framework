/**
 * ConvertException.java 2015年10月19日
 */
package org.macula.boot.core.exception;

import org.macula.boot.exception.MaculaException;

/**
 * <p>
 * <b>ConvertException</b> 类型转换异常
 * </p>
 *
 * @since 2015年10月19日
 * @author Rain
 * @version $Id: ConvertException.java 5906 2015-10-19 09:40:12Z wzp $
 */
public class ConvertException extends MaculaException {

	private static final long serialVersionUID = 1L;

	public ConvertException(Throwable e) {
		super("Convert Error", e);
	}
}
