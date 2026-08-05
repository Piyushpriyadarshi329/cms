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
public class ClientRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobile;

    @NotBlank
    private String pan;

    @NotBlank
    private String gst;

    @NotBlank
    private String address;

    private String city;

    private String state;

    private String pincode;

    private String country;
}
