/**
 * Copyright @ 2017-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.macula.cloud.dubbo.exception.translator;

import org.apache.dubbo.rpc.RpcException;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;
import org.maculaframework.boot.core.exception.translator.MaculaExceptionTranslator;
import org.maculaframework.boot.core.utils.ExceptionUtils;
import org.springframework.stereotype.Component;

import static org.maculaframework.boot.MaculaConstants.EXCEPTION_CODE_RPC;

/**
 * <p> <b>RpcExceptionTranslator</b> 将RpcException翻译成用户能理解的ServiceException。</p>
 * 
 * @since 2017年7月5日
 * @author Arron.Lin
 */
@Component
public class RpcExceptionTranslator  implements MaculaExceptionTranslator {
	

	@Override
	public int getOrder() {
		return 300;
	}
	

	@Override
	public MaculaException translateExceptionIfPossible(Throwable ex) {
		if (ExceptionUtils.getRecursionCauseException(ex, RpcException.class) != null) {
			return new ServiceException(EXCEPTION_CODE_RPC, "org.apache.dubbo.rpc.RpcException", ex);
		}
		return null;
	}
}
