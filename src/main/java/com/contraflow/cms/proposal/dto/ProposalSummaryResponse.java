package com.contraflow.cms.proposal.dto;

import com.contraflow.cms.proposal.entity.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * Lightweight list view: only the proposal's own columns + related FK ids.
 * No nested tenant/client/discussion data — fetch those via the detail endpoint
 * ({@code GET /tenant/proposal/{id}}) so the list stays a single query.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalSummaryResponse {

    private UUID id;
    private String proposalNumber;
    private String title;
    private String description;
    private Proposal.ProposalStatus status;
    private Date proposalStartDate;

    private Long tenantId;
    private Long clientId;
    private Long clientUserId;
}
