package com.contraflow.cms.security.auth;

import com.contraflow.cms.admin.entity.Admin;
import com.contraflow.cms.admin.repository.AdminRepository;
import com.contraflow.cms.security.jwt.JwtService;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final TenantUserRepository tenantUserRepository;
    private final AdminAuthService adminAuthService;
    private final TenantAuthServices tenantAuthServices;

    /**
     * Exchange a valid refresh token for a fresh access token (+ rotated refresh token).
     * Stateless: the refresh token is verified by signature + expiry + tokenType claim.
     */
    public LoginResponse refresh(String refreshToken) {

        final String email;
        final String type;
        try {
            // parsing (inside these calls) validates signature and expiry
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new BadCredentialsException("Not a refresh token");
            }
            email = jwtService.extractUsername(refreshToken);
            type = jwtService.extractUserType(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }

        if ("TENANT".equals(type)) {
            TenantUser tenantUser = tenantUserRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User no longer exists"));
            return tenantAuthServices.buildLoginResponse(tenantUser);
        }

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User no longer exists"));
        return adminAuthService.buildLoginResponse(admin);
    }
}
