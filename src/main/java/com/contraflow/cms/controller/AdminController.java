package com.contraflow.cms.controller;


import com.contraflow.cms.dto.AdminResponse;
import com.contraflow.cms.services.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
