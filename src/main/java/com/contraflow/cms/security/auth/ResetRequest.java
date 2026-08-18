package com.contraflow.cms.security.auth;

import lombok.Data;

@Data
public class ResetRequest {
    private String resetToken;
    private String password;
}
