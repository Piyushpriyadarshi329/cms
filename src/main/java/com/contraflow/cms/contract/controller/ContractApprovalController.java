package com.contraflow.cms.contract.controller;

import com.contraflow.cms.contract.dto.ContractRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/contract/{contractId}")
public class ContractApprovalController {

    @PostMapping("/manager-approve")
    public String contractManagerApprove(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }

    @PostMapping("/finance-approve")
    public String contractFinanceApprove(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }


    @PostMapping("/legal-approve")
    public String contractLegalApprove(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }


    @PostMapping("/send-esign")
    public String contractSendESign(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }

    @PostMapping("/close")
    public String contractClose(@RequestBody ContractRequest contractRequest){
        return "Convert proposal to Contract";
    }

}
