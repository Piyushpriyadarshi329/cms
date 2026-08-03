package com.contraflow.cms.aws.s3.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUploadResponse {
    private String uploadUrl;

    private String objectKey;

    private long expiresIn;


}
