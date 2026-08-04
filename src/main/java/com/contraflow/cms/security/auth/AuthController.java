package com.contraflow.cms.security.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

private final AdminAuthService adminAuthService;
private final TenantAuthServices tenantAuthServices;


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


}
