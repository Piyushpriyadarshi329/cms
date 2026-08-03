package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.proposal.dto.ProposalRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/propasal")
public class ProposalController {




    public String getProposal(@RequestBody ProposalRequest proposalRequest){
        return "proposal create";
    }


    @PostMapping("/create")
    public String createProposal(@RequestBody ProposalRequest proposalRequest){
        return "proposal create";
    }
    @PostMapping("/addDiscussion")
    public String addProposalDiscussion(@RequestBody ProposalRequest proposalRequest){
        return "proposal create";
    }






}
