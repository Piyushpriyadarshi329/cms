package com.contraflow.cms.contract.entity;


import com.contraflow.cms.proposal.Enum.BillingType;
import com.contraflow.cms.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contracts")
public class Contract {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;


    // Not unique: a proposal may have an earlier soft-deleted (reverted) contract plus a new one.
    // At most one ACTIVE (deleted=false) contract per proposal is enforced in the service layer.
    @Column(name = "proposal_id")
    private UUID proposalId;

    @Column(name = "contract_title")
    private String contractTitle;

    @Column(name = "contract_Number")
    private String contractNumber;



    @Builder.Default
    @Column(name="contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus status = ContractStatus.MANAGER_APPROVAL_PENDING;



    @Column(name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;


    // Snapshot of the latest proposal version at the time of conversion to a contract.
    @Column(name = "proposal_amount")
    private Long proposalAmount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "billing")
    @Enumerated(EnumType.STRING)
    private BillingType billing;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "proposal_version_number")
    private Integer proposalVersionNumber;

    // Soft-delete flag: set true when a contract is reverted back to its proposal.
    @Builder.Default
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

}
