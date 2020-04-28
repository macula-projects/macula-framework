/**
 * ConsumerAuthFilter.java 2015年9月23日
 */
package org.macula.cloud.dubbo.rpc.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.maculaframework.boot.MaculaConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * <b>ConsumerAuthFilter</b> 传送消费方的通用属性给提供方参考，包括用户名等
 * </p>
 *
 * @since 2015年9月23日
 * @author Rain
 * @version $Id: ConsumerAttachmentFilter.java 5937 2015-11-05 07:06:29Z wzp $
 */
@Activate(group = CommonConstants.CONSUMER)
public class ConsumerAttachmentFilter implements Filter {
	
	public final static String USER_NAME = "ConsumerUserName";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// 消费方的用户名
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = MaculaConstants.BACKGROUND_USER;
		if (authentication != null) {
			name = authentication.getName();
		}
		
		RpcContext.getContext().setAttachment(USER_NAME, name);
		return invoker.invoke(invocation);
	}

}
