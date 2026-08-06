package com.contraflow.cms.security.auth;


import com.contraflow.cms.security.jwt.JwtService;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TenantAuthServices {


    private final TenantUserRepository tenantUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request){

        // Authenticate against the tenant_user table (NOT admins)
        TenantUser tenantUser = tenantUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), tenantUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        UserDetails userDetails = User.builder()
                .username(tenantUser.getEmail())
                .password(tenantUser.getPassword())
                .roles(tenantUser.getRole() != null ? tenantUser.getRole().name() : "TENANT")
                .build();

        String role = tenantUser.getRole() != null ? tenantUser.getRole().name() : "TENANT";
        Long tenantId = tenantUser.getTenantId() != null ? tenantUser.getTenantId().getId() : null;

        // Embed user details in the token so the backend can parse them later
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "TENANT");
        claims.put("userId", tenantUser.getId());
        claims.put("firstName", tenantUser.getFirstName());
        claims.put("lastName", tenantUser.getLastName());
        claims.put("role", role);
        if (tenantId != null) claims.put("tenantId", tenantId);

        String token = jwtService.generateToken(userDetails, claims);

        return LoginResponse.builder()
                .success(true)
                .massage("Login Success")
                .token(token)
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
