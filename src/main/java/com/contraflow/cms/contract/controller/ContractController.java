package com.contraflow.cms.contract.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.contract.dto.ContractRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tenant/{tenantId}/contract")
@RestController
public class ContractController {

    @GetMapping
    public ResponseEntity<ApiResponse<Void>> getContract(@PathVariable Long tenantId){
        return ResponseEntity.ok(ApiResponse.success("Contracts fetched successfully", null));
    }



    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createContract(@RequestBody ContractRequest contractRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contract created successfully", null));
    }


}
