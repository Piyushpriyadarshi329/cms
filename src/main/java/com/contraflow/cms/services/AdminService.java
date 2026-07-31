package com.contraflow.cms.services;


import com.contraflow.cms.dto.admin.AdminRequest;
import com.contraflow.cms.dto.admin.AdminResponse;
import com.contraflow.cms.dto.admin.LoginRequest;
import com.contraflow.cms.dto.admin.LoginResponse;
import com.contraflow.cms.entity.Admin;
import com.contraflow.cms.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository){

        this.adminRepository= adminRepository;

    }


    public List<AdminResponse> getAllAdmin(){

        return adminRepository.findAll().stream().map(admin->new AdminResponse(admin.getId(),admin.getEmail(),admin.getFirstName(),admin.getLastName())

        ).toList();
    }
    public String createAdmin(AdminRequest adminRequest ){
         Admin admin = new Admin();
         admin.setFirstName(adminRequest.getFirstName());
         admin.setLastName(adminRequest.getLastName());
         admin.setEmail(adminRequest.getEmail());
         admin.setPassword(adminRequest.getPassword());
        adminRepository.save( admin);
        return "Admin Created Successfully";
    }

    public LoginResponse loginHandler(LoginRequest loginRequest){
      Optional<Admin> optionalAdmin= adminRepository.findByEmail(loginRequest.getEmail());
      if(optionalAdmin.isEmpty()){
          throw new RuntimeException("Invalid email or password");
      }
      Admin admin = optionalAdmin.get();

      if(!admin.getPassword().equals(loginRequest.getPassword())){
          throw new RuntimeException("Invalid email or password");
      }

      return LoginResponse.builder()
              .firstName(admin.getFirstName())
              .lastName(admin.getLastName())
              .email(admin.getEmail())
              .isLogin(true)
              .build();
    }
}
