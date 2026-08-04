package com.contraflow.cms.tenant.services;

import com.contraflow.cms.tenant.dto.TenantUserRequest;
import com.contraflow.cms.tenant.dto.TenantUserResponse;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantRepository;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantUserService {
    @Autowired
    private TenantUserRepository tenantUserRepository;
    @Autowired
    private TenantRepository tenantRepository;

    public List<TenantUser>getAll(){
        return tenantUserRepository.findAll();
    }

    public TenantUserResponse createUser(TenantUserRequest  request){
        Tenant tenant = tenantRepository.findById(request.getTenant_id()).orElseThrow(()->new RuntimeException("No tenant Exists"));
        TenantUser tenantUser = TenantUser.builder()
                .tenant_id(tenant)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(request.getPassword())
                .role(request.getRole())
                .created_at(request.getCreated_at())
                .build();

        TenantUser saved = tenantUserRepository.save(tenantUser);
        return TenantUserResponse.builder()
                .id(saved.getId())
                .tenant_id(saved.getTenant_id())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .mobile(saved.getMobile())
                .role(saved.getRole())
                .created_at(saved.getCreated_at())
                .last_login_at(saved.getLast_login_at())
                .build();
    }

    public TenantUser updateUser(TenantUserRequest request,Long id){
        TenantUser tenantUser = tenantUserRepository.findById(id).orElseThrow(()->new RuntimeException("No member Found"));
        tenantUser.setFirstName(request.getFirstName());
        tenantUser.setLastName(request.getLastName());
        tenantUser.setEmail(request.getEmail());
        tenantUser.setMobile(request.getMobile());
        tenantUser.setRole(request.getRole());
        return tenantUserRepository.save(tenantUser);
    }



}
