package com.contraflow.cms.signer.entity;
import com.contraflow.cms.contract.entity.Contract;
import com.contraflow.cms.signer.Enum.ESign_Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "signer")
public class Signer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "contract_url")
    private String contractUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "internal_sign_by")
    private String internalSignBy;

    @Column(name = "internal_sign_at")
    private LocalDateTime internalSignAt;

    @Column(name = "internal_sign_url")
    private String internalSignUrl;

    @Column(name = "client_sign_by")
    private String clientSignBy;

    @Column(name = "client_sign_at")
    private LocalDateTime clientSignAt;

    @Column(name = "client_sign_url")
    private String clientSignUrl;

    @Column(name = "e_sign_url")
    private String eSignUrl;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expires_at")
    private LocalDateTime otpExpiresAt;

    @Column(name = "otp_token")
    private String otpToken;

    @Column(name = "otp_token_expires_at")
    private LocalDateTime otpTokenExpiresAt;

    @Enumerated(EnumType.STRING)
    private ESign_Status status;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean not null default false")
    private boolean deleted = false;
}