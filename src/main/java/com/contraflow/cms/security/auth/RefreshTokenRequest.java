package com.contraflow.cms.security.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
