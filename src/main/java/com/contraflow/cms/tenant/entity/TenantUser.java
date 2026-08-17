package com.contraflow.cms.tenant.entity;

import com.contraflow.cms.tenant.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="tenant_user")
public class TenantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="tenant_id",nullable = false,updatable = false)
    private Tenant tenantId;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String mobile;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean not null default false")
    private boolean deleted = false;

    @Column(nullable = false,updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
    }


    private LocalDateTime last_login_at;

    private String otp;
}
