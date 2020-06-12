/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.security.web.config;

import org.maculaframework.boot.web.config.WebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * <b>SecurityWebAutoConfig</b> WEB安全自动配置
 * </p>
 *
 * @author Rain
 * @since 2020-02-28
 */
@Configuration
@AutoConfigureAfter({WebAutoConfiguration.class})
@Import({ RedisHttpSessionConfig.class, KaptchaConfig.class,
    AuthenticationConfig.class, MethodSecurityConfig.class, WebSecurityConfig.class})
public class SecurityWebAutoConfiguration {
}
