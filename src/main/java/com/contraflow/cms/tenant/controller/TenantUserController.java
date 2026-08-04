package com.contraflow.cms.tenant.controller;

import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/tenant/user")
public class TenantUserController {

    @Autowired
    private TenantService tenantService;



}
