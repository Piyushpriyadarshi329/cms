package com.contraflow.cms.tenant.mapper;

import com.contraflow.cms.aws.s3.services.S3Service;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TenantMapper {

    public final S3Service s3Service;



    public TenantResponse toResponse(Tenant tenant){

        return TenantResponse.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .legalName(tenant.getLegalName())
                // logoUrl is stored as an S3 object key; return a usable presigned URL
                .logoUrl(s3Service.getDownloadUrl(tenant.getLogoUrl()))
                .mobile(tenant.getMobile())
                .email(tenant.getEmail())
                .address(tenant.getAddress())
                .state(tenant.getState())
                .city(tenant.getCity())
                .pinCode(tenant.getPinCode())
                .country(tenant.getCountry())
                .verified(tenant.getVerified())
                .createdAt(tenant.getCreatedAt())
                .build();
    }

}
