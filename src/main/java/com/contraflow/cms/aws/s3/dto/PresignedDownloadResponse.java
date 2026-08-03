package com.contraflow.cms.aws.s3.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedDownloadResponse {

    private String downloadUrl;

    private long expiresIn;

}
