package com.contraflow.cms.contract.dto;

import com.contraflow.cms.contract.entity.ContractStatus;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

public class ContractResponse {


    private UUID Id;
    private Long tenantId;
    private ProposalResponse proposal;
    private String contractTitle;
    private ContractStatus status;
    private String billingType;
    private String contractType;

}
