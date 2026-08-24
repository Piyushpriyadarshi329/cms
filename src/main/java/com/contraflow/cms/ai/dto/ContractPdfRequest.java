package com.contraflow.cms.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ContractPdfRequest {

    @NotNull(message = "Contract id is required")
    private UUID contractId;
}
