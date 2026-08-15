package com.contraflow.cms.contract.dto;

import com.contraflow.cms.contract.entity.ContractStatus;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.entity.Tenant;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {


    private UUID Id;
    private TenantResponse tenant;
    private ProposalResponse proposal;
    private String contractTitle;
    private ContractStatus status;
    private String billingType;
    private String contractType;

}
