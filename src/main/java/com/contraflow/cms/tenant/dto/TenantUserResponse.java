package com.contraflow.cms.tenant.dto;

import com.contraflow.cms.tenant.Enum.Role;
import com.contraflow.cms.tenant.entity.Tenant;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="tenant_id",nullable = false)
    private Tenant tenant;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false,updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
    }

    private LocalDateTime last_login_at;
}
