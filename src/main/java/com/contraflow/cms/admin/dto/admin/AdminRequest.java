package com.contraflow.cms.admin.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdminRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "Email name is required")
    private String email;

    @NotBlank(message = "password name is required")
    private String password;
}
