package com.contraflow.cms.tenant.services;

import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.tenant.dto.TenantUserRequest;
import com.contraflow.cms.tenant.dto.TenantUserResponse;
import com.contraflow.cms.tenant.dto.ThemeConfig;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantRepository;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantUserService {
    @Autowired
    private TenantUserRepository tenantUserRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<TenantUserResponse> getAll() {

        return tenantUserRepository.findAll()
                .stream()
                .map(user -> TenantUserResponse.builder()
                        .id(user.getId())
                        .tenant(user.getTenantId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .mobile(user.getMobile())
                        .role(user.getRole())
                        .created_at(user.getCreated_at())
                        .last_login_at(user.getLast_login_at())
                        .build())
                .toList();
    }

    public TenantUserResponse createUser(Long tenantId,   TenantUserRequest  request){
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()->new ResourceNotFoundException("No tenant Exists"));
        TenantUser tenantUser = TenantUser.builder()
                .tenantId(tenant)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .created_at(request.getCreated_at())
                .build();

        TenantUser saved = tenantUserRepository.save(tenantUser);
        return TenantUserResponse.builder()
                .id(saved.getId())
                .tenant(saved.getTenantId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .mobile(saved.getMobile())
                .role(saved.getRole())
                .created_at(saved.getCreated_at())
                .last_login_at(saved.getLast_login_at())
                .build();
    }

    public ThemeConfig getMyTheme(String email){
        TenantUser tenantUser = tenantUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tenant user not found"));
        return tenantUser.getTenantId().getThemeConfig();
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
