package com.contraflow.cms.tenant.entity;


import com.contraflow.cms.tenant.dto.ThemeConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    private String legalName;

    private String logoUrl;

    //String - as it can have +91 - special char also starting with different country format
    //also not going to perform calculation
    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobile;

    @NotBlank
    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$")
    private String pinCode;

    @NotBlank
    private String country;

    @NotNull
    private Boolean verified;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean not null default false")
    private boolean deleted = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private ThemeConfig themeConfig;

    @Column(nullable = true,updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
