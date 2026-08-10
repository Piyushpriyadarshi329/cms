package com.contraflow.cms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Convenience accessors for the current authenticated user (AuthUser) anywhere
 * in the code — e.g. inside a service where @AuthenticationPrincipal isn't available.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthUser user) {
            return user;
        }
        return null;
    }

    public static Long currentTenantId() {
        AuthUser user = currentUser();
        return user != null ? user.getTenantId() : null;
    }

    public static Long currentUserId() {
        AuthUser user = currentUser();
        return user != null ? user.getUserId() : null;
    }
}
