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

package org.maculaframework.boot.core.config.core;

import org.maculaframework.boot.MaculaConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * <b>CoreConfigProperties</b> 属性设置
 * </p>
 *
 * @author Rain
 * @since 2019-01-23
 */
@ConfigurationProperties(prefix = MaculaConstants.CONFIG_BOOT_CORE_PREFIX)
public class CoreConfigProperties {

    // XSS防护级别，默认是BASIC级别
    private static MaculaConstants.ESCAPE_XSS_LEVEL escapeXssLevel = MaculaConstants.ESCAPE_XSS_LEVEL.BASIC;

    // 是否开启XSS防护，默认开启
    private static boolean enableEscapeXss = true;

    public static MaculaConstants.ESCAPE_XSS_LEVEL getEscapeXssLevel() {
        return escapeXssLevel;
    }

    public void setEscapeXssLevel(MaculaConstants.ESCAPE_XSS_LEVEL escapeXssLevel) {
        CoreConfigProperties.escapeXssLevel = escapeXssLevel;
    }

    public static boolean isEnableEscapeXss() {
        return enableEscapeXss;
    }

    public void setEnableEscapeXss(boolean enableEscapeXss) {
        CoreConfigProperties.enableEscapeXss = enableEscapeXss;
    }
}
