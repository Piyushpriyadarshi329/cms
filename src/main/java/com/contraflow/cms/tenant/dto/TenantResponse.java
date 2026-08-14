package com.contraflow.cms.tenant.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantResponse {

    private Long id;
    private String name;
    private String legalName;
    private String logoUrl;
    private String mobile;
    private String email;
    private String address;
    private String state;
    private String city;
    private String pinCode;
    private String country;
    private Boolean verified;
    private LocalDateTime createdAt;
}