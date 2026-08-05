package com.contraflow.cms.contract.controller;


import com.contraflow.cms.contract.dto.ContractRequest;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tenant/{tenantId}/contract")
@RestController
public class ContractController {

    @GetMapping
    public String getContract( @PathVariable Long tenantId ){
        return "fetch Contract by tenant id";
    }



    @PostMapping
    public String createContract(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }


}
