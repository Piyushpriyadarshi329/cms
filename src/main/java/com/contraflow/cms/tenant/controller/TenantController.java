package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.tenant.dto.TenantRequest;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.dto.ThemeConfig;
import com.contraflow.cms.tenant.dto.TenantThemeRequest;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.services.TenantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;


    @PostMapping
    public TenantResponse createTenant(@Valid @RequestBody TenantRequest tenantRequest){
        return  tenantService.createTenant(tenantRequest);
    }

    @GetMapping
    public List<Tenant> getAllTenants() {
        return tenantService.getAllTenants();
    }

    @GetMapping("/{id}")
    public Tenant getTenantById(@PathVariable Long id){
        return tenantService.getTenantById(id);
    }

    @PutMapping("/{id}")
    public Tenant updateTenant(@Valid @RequestBody TenantRequest tenantRequest,@PathVariable Long id){
        return tenantService.updateTenant(tenantRequest,id);
    }

    @DeleteMapping("/{id}")
    public void deleteTenant(@PathVariable Long id){
        tenantService.deleteTenant(id);
    }

    @PatchMapping("/{id}/theme")
    public ThemeConfig updateTheme(@RequestBody TenantThemeRequest request, @PathVariable Long id){
        return tenantService.updateTheme(id, request);
    }
}
