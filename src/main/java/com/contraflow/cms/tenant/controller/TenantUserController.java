package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.tenant.dto.TenantUserRequest;
import com.contraflow.cms.tenant.dto.TenantUserResponse;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.services.TenantService;
import com.contraflow.cms.tenant.services.TenantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tenant/user")
public class TenantUserController {

    @Autowired
    private TenantUserService tenantUserService;

    @GetMapping
    public List<TenantUser> getAllTenants(){
        return tenantUserService.getAll();
    }

    @PostMapping
    public TenantUserResponse createUser(@RequestBody TenantUserRequest request){
        return tenantUserService.createUser(request);
    }

    @PutMapping("/{id}")
    public TenantUser updateService(@RequestBody TenantUserRequest request,@PathVariable Long id){
        return tenantUserService.updateUser(request,id);
    }

}
