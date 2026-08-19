package com.contraflow.cms.security.auth;

import lombok.Data;

@Data
public class ValidateRequest {
    private String email;
    private String otp;
}
