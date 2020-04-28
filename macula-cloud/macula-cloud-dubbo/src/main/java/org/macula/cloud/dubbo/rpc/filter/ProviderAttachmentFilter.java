/**
 * ProviderAttachmentFilter.java 2015年9月23日
 */
package org.macula.cloud.dubbo.rpc.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * <b>ProviderAttachmentFilter</b> 提供方获取消费方通过RpcContext传过来的参数并做相应处理
 * </p>
 *
 * @since 2015年9月23日
 * @author Rain
 * @version $Id: ProviderAttachmentFilter.java 5937 2015-11-05 07:06:29Z wzp $
 */
@Activate(group = CommonConstants.PROVIDER)
public class ProviderAttachmentFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// 消费方的用户名
		String userName = RpcContext.getContext().getAttachment(ConsumerAttachmentFilter.USER_NAME);
		
		// 提供给后台更新lastUpdatedBy和createBy
		if (StringUtils.isNotEmpty(userName)) {
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userName, null));
		}
		return invoker.invoke(invocation);
	}

}
