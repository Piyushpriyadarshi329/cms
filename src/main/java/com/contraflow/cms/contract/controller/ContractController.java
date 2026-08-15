package com.contraflow.cms.contract.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.contract.service.ContractService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<List<ContractResponse>>> createContract(@AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();

        List<ContractResponse> contractResponseList= contractService.getContract(tenantId);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Contract Fetch successfully",  contractResponseList));
    }


}
