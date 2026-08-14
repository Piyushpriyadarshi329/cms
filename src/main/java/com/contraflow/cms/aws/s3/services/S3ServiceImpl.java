package com.contraflow.cms.aws.s3.services;


import com.contraflow.cms.aws.s3.dto.PresignedDownloadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedDownloadResponse;
import com.contraflow.cms.aws.s3.dto.PresignedUploadRequest;
import com.contraflow.cms.aws.s3.dto.PresignedUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3ServiceImpl implements S3Service {

    private final S3Presigner presigner;

    @Value("${aws.bucket-name}")
    private String bucketName;



     @Override
    public PresignedUploadResponse generateUploadUrl(PresignedUploadRequest presignedUploadRequest){


         String objectKey =
                 "proposal/" +
                         UUID.randomUUID() +
                         "-" +
                         presignedUploadRequest.getFileName();

         // Note: content-type is intentionally NOT set here. Setting it makes S3
         // sign the content-type header, so the client's PUT Content-Type must then
         // match byte-for-byte or S3 returns SignatureDoesNotMatch. Leaving it unsigned
         // lets the client send any Content-Type; S3 still stores whatever it sends.
         PutObjectRequest putObjectRequest =
                 PutObjectRequest.builder()
                         .bucket(bucketName)
                         .key(objectKey)
                         .build();

         PutObjectPresignRequest presignRequest =
                 PutObjectPresignRequest.builder()
                         .signatureDuration(Duration.ofMinutes(15))
                         .putObjectRequest(putObjectRequest)
                         .build();

         PresignedPutObjectRequest presignedRequest =
                 presigner.presignPutObject(presignRequest);

         return new PresignedUploadResponse(
                 presignedRequest.url().toString(),
                 objectKey,
                 900
         );

    }

    @Override
    public PresignedDownloadResponse generateDownloadUrl(
            PresignedDownloadRequest request) {

        return new PresignedDownloadResponse(
                getDownloadUrl(request.getObjectKey()),
                900
        );
    }

    @Override
    public String getDownloadUrl(String objectKey) {
         System.out.println("objectKey"+objectKey);
        if (objectKey == null || objectKey.isBlank()) {
            return null;
        }

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(15))
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

}
