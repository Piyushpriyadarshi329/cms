package com.contraflow.cms.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClientUserResponse {
    private Long id;

    private Long clientId;

    private String firstname;

    private String lastname;

    private String mobile;

    private String email;

    private Boolean active;
}
