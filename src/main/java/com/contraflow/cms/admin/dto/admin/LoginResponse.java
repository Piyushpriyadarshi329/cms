package com.contraflow.cms.admin.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String email;
    @Builder.Default
    private boolean isLogin = false;

}
