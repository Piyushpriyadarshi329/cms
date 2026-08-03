package com.contraflow.cms.client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "tenant_id")
    private Long tenantId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false,unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @Column(nullable = false, unique = true)
    private String mobile;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String pan;

    @NotBlank
    @Column(nullable = false)
    private String gst;

    @NotBlank
    @Column(nullable = false)
    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
