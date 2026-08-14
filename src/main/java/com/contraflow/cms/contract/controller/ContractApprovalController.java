package com.contraflow.cms.contract.controller;

import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/contract/{contractId}")
public class ContractApprovalController {

    @PostMapping("/manager-approve")
    public ResponseEntity<ApiResponse<Void>> contractManagerApprove(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.ok(ApiResponse.success("Contract approved by manager", null));
    }

    @PostMapping("/finance-approve")
    public ResponseEntity<ApiResponse<Void>> contractFinanceApprove(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.ok(ApiResponse.success("Contract approved by finance", null));
    }


    @PostMapping("/legal-approve")
    public ResponseEntity<ApiResponse<Void>> contractLegalApprove(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.ok(ApiResponse.success("Contract approved by legal", null));
    }


    @PostMapping("/send-esign")
    public ResponseEntity<ApiResponse<Void>> contractSendESign(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.ok(ApiResponse.success("Contract sent for e-sign", null));
    }

    @PostMapping("/close")
    public ResponseEntity<ApiResponse<Void>> contractClose(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.ok(ApiResponse.success("Contract closed", null));
    }

}
