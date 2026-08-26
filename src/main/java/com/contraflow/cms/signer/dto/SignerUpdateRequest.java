package com.contraflow.cms.signer.dto;

import com.contraflow.cms.signer.Enum.ESign_Status;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Request body for completing a sign step. otpToken is required — it proves
 * the caller just verified an OTP for this Signer; without it the sign
 * fields/status below cannot be applied.
 */
@Data
public class SignerUpdateRequest {

    private String otpToken;

    private String contractUrl;

    private String internalSignBy;
    private LocalDateTime internalSignAt;
    private String internalSignUrl;

    private String clientSignBy;
    private LocalDateTime clientSignAt;
    private String clientSignUrl;

    private String eSignUrl;
    private ESign_Status eSignStatus;
}
