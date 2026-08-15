package com.contraflow.cms.contract.entity;

import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import jakarta.persistence.*;
import lombok.*;
import com.contraflow.cms.contract.entity.ApprovalAction;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "contract_approval_history",
        indexes = {
                @Index(name = "idx_contract_approval_tenant", columnList = "tenant_id"),
                @Index(name = "idx_contract_approval_contract", columnList = "contract_id"),
                @Index(name = "idx_contract_approval_action_at", columnList = "action_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.contraflow.cms.contract.entity.ApprovalAction action;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "action_by", nullable = false)
    private TenantUser actionBy;

    @Column(name = "action_at", nullable = false)
    private LocalDateTime actionAt;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (actionAt == null) {
            actionAt = createdAt;
        }
    }
}