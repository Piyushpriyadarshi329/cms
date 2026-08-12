package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.admin.dto.ApiResponse;
import com.contraflow.cms.tenant.dto.ThemeConfig;
import com.contraflow.cms.tenant.services.TenantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant-user")
public class TenantUserThemeController {

    @Autowired
    private TenantUserService tenantUserService;

    @GetMapping("/theme")
    public ResponseEntity<ApiResponse<ThemeConfig>> getMyTheme(@AuthenticationPrincipal UserDetails principal){
        ThemeConfig theme = tenantUserService.getMyTheme(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Theme fetched successfully", theme));
    }
}
