package com.contraflow.cms.contract.dto;


import com.contraflow.cms.contract.entity.ContractStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;



@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    @NotBlank(message = "contract Title is Required Field")
    private String contractTitle;


    @NotNull(message = "proposal Id is Required Field")
    private UUID proposalId;

    @Builder.Default
    private ContractStatus status = ContractStatus.MANAGER_APPROVAL_PENDING;


    @NotBlank(message = "billing Type is Required Field")
    private String billingType;

    @NotBlank(message = "contract Type is Required Field")
    private String contractType ;

}
