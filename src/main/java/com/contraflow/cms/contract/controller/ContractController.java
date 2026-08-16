package com.contraflow.cms.contract.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.dto.ContractRevertRequest;
import com.contraflow.cms.contract.dto.ContractSummaryResponse;
import com.contraflow.cms.contract.service.ContractService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/tenant/contract")
@RestController
@RequiredArgsConstructor
public class ContractController {

    public final ContractService contractService;



    @PostMapping
    public ResponseEntity<ApiResponse<Void>> getContract(@AuthenticationPrincipal AuthUser authUser, @RequestBody ContractRequest request){
    Long tenantId = authUser.getTenantId();
    Long UserId = authUser.getUserId();
        contractService.createContract(tenantId, UserId,request);
        return ResponseEntity.ok(ApiResponse.success("Contracts create successfully", null));


    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<ContractSummaryResponse>>> getContracts(@AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();

        List<ContractSummaryResponse> contractResponseList = contractService.getContract(tenantId);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Contract Fetch successfully",  contractResponseList));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContractResponse>> getContractDetail(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();

        ContractResponse detail = contractService.getContractDetail(tenantId, id);

        return ResponseEntity.ok(ApiResponse.success("Contract details fetched successfully", detail));
    }


    @PostMapping("/{id}/revert")
    public ResponseEntity<ApiResponse<Void>> revertToProposal(
            @PathVariable UUID id,
            @RequestBody(required = false) ContractRevertRequest request,
            @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        Long userId = authUser.getUserId();

        contractService.revertToProposal(tenantId, userId, id, request);

        return ResponseEntity.ok(ApiResponse.success("Contract reverted to proposal successfully", null));
    }


}
