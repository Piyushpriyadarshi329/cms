package com.contraflow.cms.tenant.services;

import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.exception.DuplicateResourceException;
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
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.security.SecureRandom;
import java.util.List;

@Service
public class TenantUserService {
    @Autowired
    private TenantUserRepository tenantUserRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp() {
        int otp = secureRandom.nextInt(1_000_000);
        return String.format("%06d", otp);
    }


    public List<TenantUserResponse> getAll(Long tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new ResourceNotFoundException("Tenant not found with id : " + tenantId);
        }

        return tenantUserRepository.findByTenantId_IdAndDeletedFalse(tenantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TenantUserResponse createUser(Long tenantId,   TenantUserRequest  request){
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()->new ResourceNotFoundException("No tenant Exists"));

        if (tenantUserRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Tenant user with email " + request.getEmail() + " already exists");
        }

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
        return toResponse(saved);
    }

    public ThemeConfig getMyTheme(String email){
        TenantUser tenantUser = tenantUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant user not found with email : " + email));
        return tenantUser.getTenantId().getThemeConfig();
    }

    public TenantUserResponse updateUser(Long tenantId, Long id, TenantUserRequest request){
        TenantUser tenantUser = tenantUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant user not found with id : " + id));

        if (!tenantUser.getTenantId().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Tenant user not found with id : " + id);
        }

        tenantUser.setFirstName(request.getFirstName());
        tenantUser.setLastName(request.getLastName());
        tenantUser.setEmail(request.getEmail());
        tenantUser.setMobile(request.getMobile());
        tenantUser.setRole(request.getRole());
        return toResponse(tenantUserRepository.save(tenantUser));
    }

    public void deleteUser(Long tenantId, Long id) {
        TenantUser tenantUser = tenantUserRepository
                .findByTenantId_IdAndIdAndDeletedFalse(tenantId, id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tenant user not found with id : " + id
                        )
                );

        tenantUser.setDeleted(true);

        tenantUserRepository.save(tenantUser);
    }

    private TenantUserResponse toResponse(TenantUser user){
        return TenantUserResponse.builder()
                .id(user.getId())
                .tenant(user.getTenantId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .last_login_at(user.getLast_login_at())
                .build();
    }

    public String sendOtp(String email){
        TenantUser tenantUser = tenantUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + email));
        tenantUser.setOtp(generateOtp());
        tenantUserRepository.save(tenantUser);
        return "OTP sent successfully";
    }

    public String validateOtp(String otp, String email){
        TenantUser tenantUser = tenantUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + email));
        String otpSaved =  tenantUser.getOtp();
        if(otpSaved.equals(otp)){
          return "OTP Verified";
        }
        return "Invalid OTP";
    }

    public String reset(String email, String password){
        TenantUser tenantUser = tenantUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + email));
        tenantUser.setPassword(passwordEncoder.encode(password));
        tenantUserRepository.save(tenantUser);
        return "Password Changed Successfully";
    }
}
