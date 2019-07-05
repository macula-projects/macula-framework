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

package org.maculaframework.boot.core.repository.domain.support;

import org.maculaframework.boot.MaculaConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Stub implementation for {@link AuditorAware}. Returns {@literal null} for the
 * current auditor.
 *
 * @author Oliver Gierke
 */
public class AuditorAwareStub implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 获取当前用户登录名
        return Optional.of(getCurrentUser());
    }

    public static String getCurrentUser() {
        String name = MaculaConstants.BACKGROUND_USER;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            name = authentication.getName();
        }
        return name;
    }
}
