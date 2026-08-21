package com.contraflow.cms.signer.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignerFetchResponse {

    private Long id;

    private Long tenantId;

    private UUID contractId;

    private String contractUrl;

    private String internalSignBy;

    private LocalDateTime internalSignAt;

    private String internalSignUrl;

    private String clientSignBy;

    private LocalDateTime clientSignAt;

    private String clientSignUrl;

    private String eSignUrl;
}