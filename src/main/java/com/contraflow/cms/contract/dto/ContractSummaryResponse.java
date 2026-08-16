package com.contraflow.cms.contract.dto;

import com.contraflow.cms.contract.entity.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Lightweight list view: only the contract's own columns + related FK ids.
 * No nested tenant/proposal/timeline data — fetch those via the detail endpoint
 * ({@code GET /tenant/contract/{id}}) so the list stays a single query.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractSummaryResponse {

    private UUID id;
    private String contractNumber;
    private String contractTitle;
    private ContractStatus status;
    private String contractType;

    private UUID proposalId;
    private Long tenantId;
}
