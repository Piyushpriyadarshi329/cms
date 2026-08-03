package com.contraflow.cms.aws.s3.services;


import com.contraflow.cms.aws.s3.dto.PresignedDownloadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedDownloadResponse;
import com.contraflow.cms.aws.s3.dto.PresignedUploadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedUploadResponse;

public interface S3Service {

    PresignedUploadResponse generateUploadUrl(PresignedUploadRequest request);
    PresignedDownloadResponse generateDownloadUrl(PresignedDownloadRequest request);
}
