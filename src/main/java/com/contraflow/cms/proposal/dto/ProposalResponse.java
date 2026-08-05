package com.contraflow.cms.proposal.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponse {
    
    private UUID id;
    private String proposalNumber;
    private String title;
    private String description;
    private Long tenantId;
    private String tenantName;
    private Long clientId;
    private String clientName;
    private Long clientUserId;
    private Date proposalStartDate;
    private ProposalDiscussionResponse proposalDiscussion;
}
