package com.contraflow.cms.security.auth;


import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.client.services.ClientService;
import com.contraflow.cms.security.jwt.JwtService;
import com.contraflow.cms.security.jwt.TokenBlacklistService;
import com.contraflow.cms.tenant.services.TenantService;
import com.contraflow.cms.tenant.services.TenantUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

private final AdminAuthService adminAuthService;
private final TenantAuthServices tenantAuthServices;
private final RefreshTokenService refreshTokenService;
private final JwtService jwtService;
private final StringRedisTemplate redisTemplate;
private final TokenBlacklistService tokenBlacklistService;
private final TenantUserService tenantUserService;



@PostMapping("/register")
public RegisterResponse register(@Valid @RequestBody RegisterRequest request){

    return adminAuthService.register(request);

}


@PostMapping("admin/login")
public LoginResponse adminLogin(@RequestBody LoginRequest request){
    return adminAuthService.login(request);
}



    @PostMapping("tenant/login")
    public LoginResponse tenantLogin(@RequestBody LoginRequest request){
        return tenantAuthServices.login(request);
    }



    @PostMapping("admin/logout")
    public ResponseEntity<String> adminLogout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        // Must be present AND start with "Bearer " before we strip it
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token missing");
        }

        String token = header.substring(7);

        String jti;
        long remainingTime;
        try {
            jti = jwtService.extractJti(token);
            remainingTime = jwtService.getRemainingExpirationTime(token);
        } catch (Exception e) {
            // token wasn't a valid JWT -> nothing to blacklist
            return ResponseEntity.badRequest().body("Invalid token");
        }

        // Blacklist by jti (short id), not the whole token. Only if not already expired.
        if (jti != null && remainingTime > 0) {
            tokenBlacklistService.blacklistToken(jti,remainingTime);
        }
        return ResponseEntity.ok("Logout Successfully");
    }


    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request){
        return refreshTokenService.refresh(request.getRefreshToken());
    }

    @PostMapping("/otp")
    public String sendOtp(@RequestBody OtpRequest request){
        return tenantUserService.sendOtp(request.getEmail());
    }

    @PostMapping("/validate")
    public String validateOtp(@RequestBody ValidateRequest request){
       return tenantUserService.validateOtp(request.getOtp(),request.getEmail());
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestBody ResetRequest request){
    return tenantUserService.reset(request.getEmail(),request.getPassword());
    }
}
