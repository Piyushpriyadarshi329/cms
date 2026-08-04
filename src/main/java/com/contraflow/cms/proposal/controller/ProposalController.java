package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.proposal.dto.ProposalRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/propasal")
public class ProposalController {






    @PostMapping("/")
    public String createProposal(@RequestBody ProposalRequest proposalRequest){
        return "proposal create";
    }



    @PostMapping("/id:discussion")
    public String createProposalDiscussion(@RequestBody ProposalRequest proposalRequest){
        return "proposal discussion create";
    }


    @GetMapping("/")
    public String getProposal(){
        return "fetch proposal";
    }


    @GetMapping("/id:discussion")
    public String getProposalDiscussion(){
        return "fetch proposal discussion";
    }




}
