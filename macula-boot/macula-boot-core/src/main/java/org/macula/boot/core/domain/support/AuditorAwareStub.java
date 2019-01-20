package org.macula.boot.core.domain.support;

import org.macula.boot.core.MaculaConstants;
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

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor()
     */
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
