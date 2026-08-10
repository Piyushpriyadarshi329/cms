package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposal")
@RequiredArgsConstructor
public class ProposalController {



  public final ProposalService proposalServices;

    @PostMapping
    public ProposalResponse createProposal(@RequestBody ProposalRequest proposalRequest){

        return proposalServices.createProposal(proposalRequest) ;
    }



    @PostMapping("/id:discussion")
    public String createProposalDiscussion(@RequestBody ProposalRequest proposalRequest){
        return "proposal discussion create";
    }


    @GetMapping
    public List< ProposalResponse> getProposal(){

        return proposalServices.getAllProposals();
    }


    @GetMapping("/id:discussion")
    public String getProposalDiscussion(){
        return "fetch proposal discussion";
    }




}
