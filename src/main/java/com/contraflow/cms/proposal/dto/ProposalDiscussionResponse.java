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
public class ProposalDiscussionResponse {
    private UUID id;
    private UUID proposalId;
    private Long tenantUserId;
    private Long clientContactId;
    private Date meetingDate;
    private String title;
    private String description;
    private String remarks;
    private String requirement;
    
}
