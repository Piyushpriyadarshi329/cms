package com.contraflow.cms.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPdfResponse {

    private String html;       // the generated contract HTML
    private String objectKey;  // S3 object key of the stored PDF
    private String pdfUrl;     // presigned URL to download the PDF
}
