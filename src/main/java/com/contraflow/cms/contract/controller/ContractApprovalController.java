package com.contraflow.cms.contract.controller;

import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractApprovalRequest;
import com.contraflow.cms.contract.service.ContractApprovalService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("tenant/contract/{contractId}")
@RequiredArgsConstructor
public class ContractApprovalController {

    private final ContractApprovalService contractApprovalService;

    @PostMapping("/manager-approve")
    public ResponseEntity<ApiResponse<Void>> contractManagerApprove(
            @PathVariable UUID contractId,
            @RequestBody(required = false) ContractApprovalRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        contractApprovalService.managerApprove(
                authUser.getTenantId(), authUser.getUserId(), contractId, comment(request));
        return ResponseEntity.ok(ApiResponse.success("Contract approved by manager", null));
    }

    @PostMapping("/finance-approve")
    public ResponseEntity<ApiResponse<Void>> contractFinanceApprove(
            @PathVariable UUID contractId,
            @RequestBody(required = false) ContractApprovalRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        contractApprovalService.financeApprove(
                authUser.getTenantId(), authUser.getUserId(), contractId, comment(request));
        return ResponseEntity.ok(ApiResponse.success("Contract approved by finance", null));
    }




//    add esignRequest for internal

    @PostMapping("/legal-approve")
    public ResponseEntity<ApiResponse<Void>> contractLegalApprove(
            @PathVariable UUID contractId,
            @RequestBody(required = false) ContractApprovalRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        contractApprovalService.legalApprove(
                authUser.getTenantId(), authUser.getUserId(), contractId, comment(request));
        return ResponseEntity.ok(ApiResponse.success("Contract approved by legal", null));
    }


    @PostMapping("/esign")
    public ResponseEntity<ApiResponse<Void>> contractSendESign(
            @PathVariable UUID contractId,
            @RequestBody(required = false) ContractApprovalRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        contractApprovalService.sendForEsign(
                authUser.getTenantId(), authUser.getUserId(), contractId, comment(request));
        return ResponseEntity.ok(ApiResponse.success("Contract sent for e-sign", null));
    }

//    @PostMapping("/request-esign")

    @PostMapping("/close")
    public ResponseEntity<ApiResponse<Void>> contractClose(
            @PathVariable UUID contractId,
            @RequestBody(required = false) ContractApprovalRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        contractApprovalService.close(
                authUser.getTenantId(), authUser.getUserId(), contractId, comment(request));
        return ResponseEntity.ok(ApiResponse.success("Contract closed", null));
    }

    private String comment(ContractApprovalRequest request) {
        return request != null ? request.getComment() : null;
    }

}
