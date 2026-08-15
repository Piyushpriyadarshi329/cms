package com.contraflow.cms.contract.entity;


import com.contraflow.cms.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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


    @Column(name = "proposal_id")
    private UUID proposalId;

    @Column(name = "contract_title")
    private String contractTitle;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(name = "billing_type")
    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    @Column(name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;



}
