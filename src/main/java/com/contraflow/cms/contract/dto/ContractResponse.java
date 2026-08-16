package com.contraflow.cms.contract.dto;

import com.contraflow.cms.contract.entity.ContractStatus;
import com.contraflow.cms.proposal.Enum.BillingType;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalVersionResponse;
import com.contraflow.cms.tenant.dto.TenantResponse;
import lombok.*;

import java.util.Date;
import java.util.List;
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
    private String contractType;

    // Snapshot of the latest proposal version stored on the contract at conversion time.
    private Long proposalAmount;
    private String currency;
    private BillingType billing;
    private Date startDate;
    private Date endDate;
    private Integer proposalVersionNumber;

    private List< TimeLineResponse> timeLine;
    private List<ProposalVersionResponse> proposalVersions;

}
