/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.maculaframework.boot.web.mvc.annotation;

import org.springframework.web.context.request.NativeWebRequest;

/**
 * <p> <b>TokenValidation</b> 是表单重复提交校验支持. </p>
 * 
 * @since 2012-2-7
 * @author Wilson Luo
 * @version $Id: TokenValidation.java 3852 2012-12-14 08:15:36Z wilson $
 */
public interface TokenValidation {

	/** 创建一个Token */
	String createTokenId(NativeWebRequest webRequest);

	/** 创建如果需要用户反馈的信息值 */
	String createResponse(String tokenId);

	/** 校验Token，同时销毁 */
	boolean validate(FormBean formBeanAnnotation, NativeWebRequest webRequest);

	/** 同时销毁 */
	boolean destoryToken(String tokenId);
}
