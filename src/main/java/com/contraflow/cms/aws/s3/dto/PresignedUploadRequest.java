package com.contraflow.cms.aws.s3.dto;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PresignedUploadRequest {
    @NotBlank(message = "FileName is required")
    private String fileName;

    @NotBlank(message = " contentType is required")
    private String contentType;
}
