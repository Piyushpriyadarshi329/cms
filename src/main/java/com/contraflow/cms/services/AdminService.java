package com.contraflow.cms.services;


import com.contraflow.cms.dto.AdminResponse;
import com.contraflow.cms.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository){

        this.adminRepository= adminRepository;

    }


    public List<AdminResponse> getAllAdmin(){

        return adminRepository.findAll().stream().map(admin->new AdminResponse(admin.getId(),admin.getEmail(),admin.getFirstName(),admin.getLastName())

        ).toList();
    }
}
