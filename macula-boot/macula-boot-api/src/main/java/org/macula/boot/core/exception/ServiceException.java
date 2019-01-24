/**
 * ServiceException.java 2015年9月24日
 */
package org.macula.boot.core.exception;

import org.macula.boot.exception.MaculaException;

/**
 * <p>
 * <b>ServiceException</b> 服务层默认异常
 * </p>
 *
 * @since 2015年9月24日
 * @author Rain
 * @version $Id: ServiceException.java 5874 2015-09-24 11:22:28Z wzp $
 */
public class ServiceException extends MaculaException {

	private static final long serialVersionUID = 1L;

	private String code;


	/**
	 * 服务层异常
	 * @param code 异常代码
	 * @param message 异常信息
	 * @param ex 原异常
	 */
	public ServiceException(String code, String message, Throwable ex) {
		super(code + ":" + message, ex);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
