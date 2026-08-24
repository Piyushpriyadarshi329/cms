package com.contraflow.cms.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUserRequest {

    @NotBlank
    private String firstname;

    @Email
    @NotBlank
    private String lastname;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Mobile number is invalid")
    private String mobile;

    @Email
    @NotBlank
    private String email;

    private Boolean active;

}