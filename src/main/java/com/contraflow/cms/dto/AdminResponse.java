package com.contraflow.cms.dto;

import com.contraflow.cms.repository.AdminRepository;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
