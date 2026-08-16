package com.contraflow.cms.proposal.dto;

import java.util.Date;
import java.util.UUID;

import com.contraflow.cms.proposal.Enum.BillingType;
import com.contraflow.cms.proposal.validation.ValidProposalDiscussion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ValidProposalDiscussion
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDiscussionRequest {

    @NotNull(message = "Proposal id is required")
    private UUID proposalId;

    private Long clientUserId;
    private Long tenantUserId;

    @NotNull(message = "Meeting date is required")
    private Date meetingDate;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String remarks;
    private String requirement;

    @NotNull(message = "Term changed is required")
    private Boolean termChanged;

    private Date proposalStartDate;

    private Long proposalAmount;

    private BillingType billing;

    private Date startDate;

    private Date endDate;

    private Long createdBy;
}