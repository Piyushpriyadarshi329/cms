package com.contraflow.cms.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The authenticated principal placed in the SecurityContext by JwtAuthenticationFilter.
 * Carries the identity parsed from the JWT so controllers/services can read the
 * current user's id / tenantId without trusting path variables.
 *
 * Read it with @AuthenticationPrincipal AuthUser user, or via SecurityUtils.
 */
@Getter
public class AuthUser implements UserDetails {

    private final String email;       // = username (JWT subject)
    private final Long userId;
    private final Long tenantId;      // null for ADMIN users
    private final String userType;    // ADMIN / TENANT
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String email, Long userId, Long tenantId, String userType,
                    Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.userId = userId;
        this.tenantId = tenantId;
        this.userType = userType;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;   // not needed after authentication
    }

    @Override
    public String getUsername() {
        return email;
    }
    // isAccountNonExpired / isAccountNonLocked / isCredentialsNonExpired / isEnabled
    // default to true (UserDetails default methods).
}
