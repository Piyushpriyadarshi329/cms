package com.contraflow.cms.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private boolean verified=true;

    @Column(nullable = false,updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
    }



}
