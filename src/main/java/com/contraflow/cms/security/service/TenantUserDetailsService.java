package com.contraflow.cms.security.service;

import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads a {@link TenantUser} (from the tenant_user table) by email.
 * Used for tenant login and for verifying tenant JWTs.
 */
@Service
@RequiredArgsConstructor
public class TenantUserDetailsService implements UserDetailsService {

    private final TenantUserRepository tenantUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        TenantUser tenantUser = tenantUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Tenant user not found"));

        return User.builder()
                .username(tenantUser.getEmail())
                .password(tenantUser.getPassword())
                .roles(tenantUser.getRole() != null ? tenantUser.getRole().name() : "TENANT")
                .build();
    }
}
