package com.contraflow.cms.admin.services;


import com.contraflow.cms.admin.dto.admin.AdminRequest;
import com.contraflow.cms.admin.dto.admin.AdminResponse;
import com.contraflow.cms.admin.entity.Admin;
import com.contraflow.cms.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder){

        this.adminRepository= adminRepository;
        this.passwordEncoder = passwordEncoder;

    }


    public List<AdminResponse> getAllAdmin(){

        return adminRepository.findAll().stream().map(admin->new AdminResponse(admin.getId(),admin.getFirstName(),admin.getLastName(),admin.getEmail())

        ).toList();
    }





    public AdminResponse createAdmin(AdminRequest adminRequest ){
         Admin admin = new Admin();
         admin.setFirstName(adminRequest.getFirstName());
         admin.setLastName(adminRequest.getLastName());
         admin.setEmail(adminRequest.getEmail());
         admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        Admin saved = adminRepository.save(admin);
        return new AdminResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail());
    }
}
