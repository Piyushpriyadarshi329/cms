package com.contraflow.cms.proposal.dto;

import com.contraflow.cms.proposal.Enum.BillingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalVersionResponse {

    private Long id;
    private Integer proposalVersionNumber;
    private Long proposalAmount;
    private String currency;
    private BillingType billing;
    private Date startDate;
    private Date endDate;

    // flat view of the tenant user who created the version — no entity/proxy
    private Long createdById;
    private String createdByName;

    private LocalDateTime createdAt;
}
