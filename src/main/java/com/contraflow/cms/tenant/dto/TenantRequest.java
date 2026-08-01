package com.contraflow.cms.tenant.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRequest {

    @NotBlank
    private String name;

    private String legalName;

    private String logoUrl;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobile;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pinCode;

    @NotBlank
    private String country;
}