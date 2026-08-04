package com.contraflow.cms.client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="client_user")
@Builder
public class ClientUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "client_id")
    private Long clientId;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @Column(nullable = false, unique = true)
    private String mobile;

    @Email
    @NotBlank
    @Column(nullable = false,unique = true)
    private String email;

    private Boolean active;
}
