package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.service.ProposalDiscussionService;
import com.contraflow.cms.proposal.service.ProposalService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant/proposal")
@RequiredArgsConstructor
public class ProposalController {



  public final ProposalService proposalServices;
  public final ProposalDiscussionService proposalDiscussionService;




    @GetMapping
    public List< ProposalResponse> getProposal(@AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        System.out.println("tenantId"+tenantId);
        return proposalServices.getAllProposals( tenantId);
    }

    @PostMapping
    public ProposalResponse createProposal(@RequestBody ProposalRequest proposalRequest, @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        return proposalServices.createProposal(tenantId,proposalRequest) ;
    }



    @PostMapping("/{id}/discussion")
    public ResponseEntity<ProposalDiscussionResponse> createProposalDiscussion(@AuthenticationPrincipal AuthUser authUser,  @RequestBody ProposalDiscussionRequest proposalDiscussionRequest){
       Long tenantId = authUser.getTenantId();
        return ResponseEntity.ok(proposalDiscussionService.addDiscussion(tenantId,proposalDiscussionRequest));
    }





//    @GetMapping("/id:discussion")
//    public String getProposalDiscussion(){
//        return "fetch proposal discussion";
//    }




}
