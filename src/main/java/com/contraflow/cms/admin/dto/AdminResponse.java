package com.contraflow.cms.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
}
