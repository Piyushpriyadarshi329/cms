package com.contraflow.cms.proposal.dto;

import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.dto.ClientUserResponse;
import com.contraflow.cms.proposal.entity.Proposal.ProposalStatus;
import com.contraflow.cms.tenant.dto.TenantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDetailResponse {

    private UUID id;
    private String proposalNumber;
    private String title;
    private String description;
    private ProposalStatus status;
    private Date proposalStartDate;

    private TenantResponse tenant;
    private ClientResponse client;
    private ClientUserResponse clientUser;

    private List<ProposalDiscussionResponse> discussions;
    private List<ProposalVersionResponse> versions;
}
