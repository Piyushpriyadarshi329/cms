package com.contraflow.cms.aws.s3.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PresignedDownloadRequest {

    @NotBlank
    private String objectKey;

}