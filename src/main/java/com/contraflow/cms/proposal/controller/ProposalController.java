package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.service.ProposalDiscussionService;
import com.contraflow.cms.proposal.service.ProposalService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public  ResponseEntity<ApiResponse< List<ProposalResponse>>> getProposal(@AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
      List<  ProposalResponse > proposalResponse=  proposalServices.getAllProposals( tenantId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Proposal Fetch successfully", proposalResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProposalResponse>> createProposal(@RequestBody ProposalRequest proposalRequest, @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        ProposalResponse created = proposalServices.createProposal(tenantId, proposalRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proposal created successfully", created));
    }



    @PostMapping("/{id}/discussion")
    public ResponseEntity<ApiResponse<ProposalDiscussionResponse>> createProposalDiscussion(@AuthenticationPrincipal AuthUser authUser,  @RequestBody ProposalDiscussionRequest proposalDiscussionRequest){
       Long tenantId = authUser.getTenantId();
        ProposalDiscussionResponse created = proposalDiscussionService.addDiscussion(tenantId, proposalDiscussionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Discussion added successfully", created));
    }





//    @GetMapping("/id:discussion")
//    public String getProposalDiscussion(){
//        return "fetch proposal discussion";
//    }




}
