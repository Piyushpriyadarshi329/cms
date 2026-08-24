package com.contraflow.cms.contract.dto;

import com.contraflow.cms.signer.Enum.ESign_Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class LegalApprovalRequest {

    @NotNull(message = "Tenant id is required")
    private Long tenantId;
    @Enumerated(EnumType.STRING)
    private ESign_Status Internal_sign  =  ESign_Status.INTERNAL_SIGN_REQUEST;
    private String comment;
}
