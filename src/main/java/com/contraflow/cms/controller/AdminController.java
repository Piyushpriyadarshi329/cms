package com.contraflow.cms.controller;


import com.contraflow.cms.dto.admin.AdminRequest;
import com.contraflow.cms.dto.admin.AdminResponse;
import com.contraflow.cms.dto.admin.LoginRequest;
import com.contraflow.cms.dto.admin.LoginResponse;
import com.contraflow.cms.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    public AdminController(AdminService adminService){
        this.adminService= adminService;
    }


    @GetMapping()
    public List<AdminResponse> getAdmin(){

        return adminService.getAllAdmin();
    }

    @PostMapping("/create")
    public String createAdmin(@Valid @RequestBody AdminRequest adminRequest){
        adminService.createAdmin(adminRequest);
        return "Admin create successfully";
    }

    @PostMapping("/login")
    public LoginResponse loginHandler(@Valid @RequestBody LoginRequest loginRequest){
       return adminService.loginHandler(loginRequest);
    }

}
