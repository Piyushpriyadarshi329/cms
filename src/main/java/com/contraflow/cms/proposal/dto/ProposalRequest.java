package com.contraflow.cms.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "client ID is required")
    private Long clientId;


    private Long clientUserId;

    @NotNull(message = "Proposal start date is required")
    private Date proposalStartDate;

 

}
