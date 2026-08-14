package com.contraflow.cms.admin.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.admin.dto.AdminRequest;
import com.contraflow.cms.admin.dto.AdminResponse;
import com.contraflow.cms.admin.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<AdminResponse>>> getAdmin(){
        List<AdminResponse> admins = adminService.getAllAdmin();
        return ResponseEntity.ok(ApiResponse.success("Admins fetched successfully", admins));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AdminResponse>> createAdmin(@Valid @RequestBody AdminRequest adminRequest){
        AdminResponse created = adminService.createAdmin(adminRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Admin created successfully", created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(
            @PathVariable Long id
    ) {

        adminService.deleteAdmin(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin deleted successfully",
                        null
                )
        );
    }

}
