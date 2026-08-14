package com.contraflow.cms.security.auth;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.security.jwt.JwtService;
import com.contraflow.cms.security.jwt.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

private final AdminAuthService adminAuthService;
private final TenantAuthServices tenantAuthServices;
private final RefreshTokenService refreshTokenService;
private final JwtService jwtService;
private final TokenBlacklistService tokenBlacklistService;


@PostMapping("/register")
public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request){
    RegisterResponse created = adminAuthService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Registered successfully", created));
}


@PostMapping("admin/login")
public ResponseEntity<ApiResponse<LoginResponse>> adminLogin(@RequestBody LoginRequest request){
    return ResponseEntity.ok(ApiResponse.success("Login successful", adminAuthService.login(request)));
}



    @PostMapping("tenant/login")
    public ResponseEntity<ApiResponse<LoginResponse>> tenantLogin(@RequestBody LoginRequest request){
        return ResponseEntity.ok(ApiResponse.success("Login successful", tenantAuthServices.login(request)));
    }



    @PostMapping("admin/logout")
    public ResponseEntity<ApiResponse<Void>> adminLogout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        // Must be present AND start with "Bearer " before we strip it
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Token missing"));
        }

        String token = header.substring(7);

        String jti;
        long remainingTime;
        try {
            jti = jwtService.extractJti(token);
            remainingTime = jwtService.getRemainingExpirationTime(token);
        } catch (Exception e) {
            // token wasn't a valid JWT -> nothing to blacklist
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid token"));
        }

        // Blacklist by jti (short id), not the whole token. Only if not already expired.
        if (jti != null && remainingTime > 0) {
            tokenBlacklistService.blacklistToken(jti,remainingTime);
        }
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", refreshTokenService.refresh(request.getRefreshToken())));
    }
}
