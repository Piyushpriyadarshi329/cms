package com.contraflow.cms.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRevertRequest {

    /** Reason the contract is being sent back (recorded on the approval timeline). */
    private String comment;
}
