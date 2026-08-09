package com.contraflow.cms.security.auth;


import com.contraflow.cms.admin.entity.Admin;
import com.contraflow.cms.admin.repository.AdminRepository;
import com.contraflow.cms.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAuthService {


    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public RegisterResponse register(RegisterRequest request){

        if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Admin admin = new Admin();
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Admin saved = adminRepository.save(admin);

        return RegisterResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .message("User registered successfully")
                .build();
    }


    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return buildLoginResponse(admin);
    }

    // Builds access + refresh tokens and the response for an admin.
    // Reused by both login and the refresh-token flow.
    public LoginResponse buildLoginResponse(Admin admin) {

        // Embed user details in the access token so the backend can parse them later
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ADMIN");
        claims.put("userId", admin.getId());
        claims.put("firstName", admin.getFirstName());
        claims.put("lastName", admin.getLastName());
        claims.put("role", "ADMIN");

        String accessToken = jwtService.generateAccessToken(admin.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(admin.getEmail(), "ADMIN");

        return LoginResponse.builder()
                .success(true)
                .message("Login Success")
                .token(accessToken)
                .refreshToken(refreshToken)
                .user(LoginResponse.UserInfo.builder()
                        .id(admin.getId())
                        .firstName(admin.getFirstName())
                        .lastName(admin.getLastName())
                        .email(admin.getEmail())
                        .role("ADMIN")
                        .build())
                .build();
    }



}
