package com.contraflow.cms.security.auth;

import lombok.Data;

@Data
public class ResetRequest {
    private String email;
    private String password;
}
