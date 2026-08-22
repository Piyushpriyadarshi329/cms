package com.contraflow.cms.contract.dto;

import com.contraflow.cms.signer.Enum.ESign_Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class LegalApprovalRequest {

    private Long tenantId;
    @Enumerated(EnumType.STRING)
    private ESign_Status Internal_sign  =  ESign_Status.INTERNAL_SIGN_REQUEST;
    private String comment;
}
