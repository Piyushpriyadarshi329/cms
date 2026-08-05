package com.contraflow.cms.contract.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "contracts")
public class Contract {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID Id;


    @Column(name = "tenant_id")
    private Long tenantId;


    @Column(name = "proposal_id")
    private UUID proposalId;

    @Column(name = "contract_title")
    private String contractTitle;



    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(name = "billing_type")
    private String billingType;


    @Column(name = "contract_type")
    private String contractType;



}
