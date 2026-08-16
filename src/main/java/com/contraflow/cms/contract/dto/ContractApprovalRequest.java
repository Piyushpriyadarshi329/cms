package com.contraflow.cms.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractApprovalRequest {

    /** Optional note recorded on the approval timeline for this action. */
    private String comment;
}
