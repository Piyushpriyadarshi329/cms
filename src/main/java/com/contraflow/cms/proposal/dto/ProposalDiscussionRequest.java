package com.contraflow.cms.proposal.dto;

import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDiscussionRequest {

    @NotNull(message = "Proposal id is required")
    private UUID proposalId;


    private Long clientUserId;
    private Long tenantUserId;


    @NotNull(message = "Meeting date is required")
    private java.util.Date meetingDate;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String remarks;

    private String requirement;
    
}
