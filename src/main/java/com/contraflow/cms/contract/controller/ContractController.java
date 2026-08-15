package com.contraflow.cms.contract.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.contract.service.ContractService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tenant/contract")
@RestController
@RequiredArgsConstructor
public class ContractController {

    public final ContractService contractService;



    @GetMapping
    public ResponseEntity<ApiResponse<Void>> getContract(@AuthenticationPrincipal AuthUser authUser, @RequestBody ContractRequest request){
    Long tenantId = authUser.getTenantId();

        contractService.createContract(tenantId,request);

        return ResponseEntity.ok(ApiResponse.success("Contracts fetched successfully", null));


    }



    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createContract(@AuthenticationPrincipal AuthUser authUser, @RequestBody ContractRequest contractRequest){



        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contract created successfully", null));
    }


}
