/**
 * ServiceException.java 2015年9月24日
 */
package org.macula.boot.core.exception;

import org.macula.boot.ApiConstants;
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

	/**
	 * @param message
	 * @param args
	 */
	public ServiceException(String message, Throwable ex) {
		super(message, ex);
	}


	@Override
	public String getParentCode() {
		return ApiConstants.MACULA_CORE_SERVICE_CODE;
	}

}
