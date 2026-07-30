package com.contraflow.cms.entity;

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
    private String lastName;
    private String email;
    private String password;

    @Builder.Default
    private boolean verified=false;

    @Column(nullable = false,updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
    }



}
