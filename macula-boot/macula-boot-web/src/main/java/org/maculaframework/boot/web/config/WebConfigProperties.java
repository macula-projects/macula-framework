/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maculaframework.boot.web.config;

import lombok.Data;
import org.maculaframework.boot.MaculaConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * <b>WebConfigProperties</b> 安全配置属性
 * </p>
 *
 * @author Rain
 * @since 2019-07-06
 */

@ConfigurationProperties(prefix = MaculaConstants.CONFIG_BOOT_WEB_PREFIX)
@Data
public class WebConfigProperties {

    /** 不经过安全拦截的URL正则 */
    private String ignoringRegexPattern = "/public.*|/error.*|/static/.*|/favicon.ico.*|/timezone.*";

    /** 同一个用户登录的最大会话数 */
    private int maximumSessions = 1;

    /** 会话过期后跳转的URL */
    private String expiredUrl = "/login";

    /** 登录发生错误跳转的页面 */
    private String failureUrl = "/login?error";

    /** 是否把Menu code当成自己的一个角色 */
    private boolean isMenuAsRole = false;
}
