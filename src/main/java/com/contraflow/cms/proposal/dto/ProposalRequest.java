package com.contraflow.cms.proposal.dto;

import com.contraflow.cms.proposal.Enum.BillingType;
import com.contraflow.cms.tenant.entity.TenantUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @NotBlank(message = "Description is required")
    private String description;



    @NotNull(message = "client ID is required")
    private Long clientId;

    @NotNull(message = "client ID is required")
    private Long clientUserId;

    @NotNull(message = "Proposal start date is required")
    private Date proposalStartDate;


    @NotNull(message = "Proposal amount is required")
    private Long proposalAmount;

    @NotNull(message = "Billing Type is required")
    private BillingType billing;


    @NotNull(message = "Start Date is Required")
    private Date startDate;


     @NotNull(message = "End Date is required")
    private Date endDate;


    @NotNull(message = "Create By is required")
    private Long createdBy;


    private Date createdAt;




}
