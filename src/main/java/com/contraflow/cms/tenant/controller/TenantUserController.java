package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.admin.dto.ApiResponse;
import com.contraflow.cms.tenant.dto.TenantUserRequest;
import com.contraflow.cms.tenant.dto.TenantUserResponse;
import com.contraflow.cms.tenant.services.TenantUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tenant/{tenantId}/user")
public class TenantUserController {

    @Autowired
    private TenantUserService tenantUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantUserResponse>>> getAllTenantUsers(@PathVariable Long tenantId){
        List<TenantUserResponse> users = tenantUserService.getAll(tenantId);
        return ResponseEntity.ok(ApiResponse.success("Tenant users fetched successfully", users));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TenantUserResponse>> createUser(@PathVariable Long tenantId, @Valid @RequestBody TenantUserRequest request){
        TenantUserResponse created = tenantUserService.createUser(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tenant user created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TenantUserResponse>> updateUser(@PathVariable Long tenantId, @PathVariable Long id, @Valid @RequestBody TenantUserRequest request){
        TenantUserResponse updated = tenantUserService.updateUser(tenantId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Tenant user updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long tenantId,
            @PathVariable Long id
    ) {
        tenantUserService.deleteUser(tenantId, id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tenant user deleted successfully",
                        null
                )
        );
    }

}
