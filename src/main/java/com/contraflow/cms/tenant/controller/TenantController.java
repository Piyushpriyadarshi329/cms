package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.admin.dto.ApiResponse;
import com.contraflow.cms.tenant.dto.TenantRequest;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.dto.ThemeConfig;
import com.contraflow.cms.tenant.dto.TenantThemeRequest;
import com.contraflow.cms.tenant.services.TenantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;


    @PostMapping
    public ResponseEntity<ApiResponse<TenantResponse>> createTenant(@Valid @RequestBody TenantRequest tenantRequest){
        TenantResponse created = tenantService.createTenant(tenantRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tenant created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantResponse>>> getAllTenants() {
        List<TenantResponse> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(ApiResponse.success("Tenants fetched successfully", tenants));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TenantResponse>> getTenantById(@PathVariable Long id){
        TenantResponse tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(ApiResponse.success("Tenant fetched successfully", tenant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TenantResponse>> updateTenant(@Valid @RequestBody TenantRequest tenantRequest, @PathVariable Long id){
        TenantResponse updated = tenantService.updateTenant(tenantRequest, id);
        return ResponseEntity.ok(ApiResponse.success("Tenant updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTenant(@PathVariable Long id){
        tenantService.deleteTenant(id);
        return ResponseEntity.ok(ApiResponse.success("Tenant deleted successfully", null));
    }

    @PatchMapping("/{id}/theme")
    public ResponseEntity<ApiResponse<ThemeConfig>> updateTheme(@RequestBody TenantThemeRequest request, @PathVariable Long id){
        ThemeConfig updated = tenantService.updateTheme(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tenant theme updated successfully", updated));
    }
}
