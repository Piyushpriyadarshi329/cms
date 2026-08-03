package com.contraflow.cms.aws.s3.controller;


import com.contraflow.cms.aws.s3.dto.PresignedDownloadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedDownloadResponse;

import com.contraflow.cms.aws.s3.dto.PresignedUploadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedUploadResponse;
import com.contraflow.cms.aws.s3.services.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;


    @PostMapping("/presigned-upload")
    public PresignedUploadResponse createPresigned(@RequestBody PresignedUploadRequest presignedUploadRequest){
        return s3Service.generateUploadUrl(presignedUploadRequest);
    }


    @PostMapping("/presigned-download")
    public PresignedDownloadResponse generateDownloadUrl(
            @Valid @RequestBody PresignedDownloadRequest request) {

        return s3Service.generateDownloadUrl(request);

    }

}
