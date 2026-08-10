package com.contraflow.cms.security.auth;


import com.contraflow.cms.security.jwt.JwtService;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TenantAuthServices {


    private final TenantUserRepository tenantUserRepository;
    private final JwtService jwtService;
    private final AuthenticationManager tenantAuthenticationManager;

    // Explicit constructor so we can @Qualifier the tenant AuthenticationManager
    // (the admin one is @Primary, so unqualified injection would pick the wrong bean).
    public TenantAuthServices(TenantUserRepository tenantUserRepository,
                              JwtService jwtService,
                              @Qualifier("tenantAuthenticationManager") AuthenticationManager tenantAuthenticationManager) {
        this.tenantUserRepository = tenantUserRepository;
        this.jwtService = jwtService;
        this.tenantAuthenticationManager = tenantAuthenticationManager;
    }


    public LoginResponse login(LoginRequest request){

        // Authenticate against the tenant_user table via the tenant AuthenticationManager.
        // Throws BadCredentialsException on wrong email/password.
        tenantAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Password verified — load the tenant user to build the token/response.
        TenantUser tenantUser = tenantUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        return buildLoginResponse(tenantUser);
    }

    // Builds access + refresh tokens and the response for a tenant user.
    // Reused by both login and the refresh-token flow.
    public LoginResponse buildLoginResponse(TenantUser tenantUser) {

        String role = tenantUser.getRole() != null ? tenantUser.getRole().name() : "TENANT";
        Long tenantId = tenantUser.getTenantId() != null ? tenantUser.getTenantId().getId() : null;

        // Embed user details in the access token so the backend can parse them later
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "TENANT");
        claims.put("userId", tenantUser.getId());
        claims.put("firstName", tenantUser.getFirstName());
        claims.put("lastName", tenantUser.getLastName());
        claims.put("role", role);
        if (tenantId != null) claims.put("tenantId", tenantId);

        String accessToken = jwtService.generateAccessToken(tenantUser.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(tenantUser.getEmail(), "TENANT");

        return LoginResponse.builder()
                .success(true)
                .message(("Login Success")).token(accessToken)
                .refreshToken(refreshToken)
                .user(LoginResponse.UserInfo.builder()
                        .id(tenantUser.getId())
                        .firstName(tenantUser.getFirstName())
                        .lastName(tenantUser.getLastName())
                        .email(tenantUser.getEmail())
                        .role(role)
                        .tenantId(tenantId)
                        .build())
                .build();

    }


}
