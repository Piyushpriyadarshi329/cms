package com.contraflow.cms.proposal.controller;


import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.proposal.dto.ProposalDetailResponse;
import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalSummaryResponse;
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
import java.util.UUID;

@RestController
@RequestMapping("/tenant/proposal")
@RequiredArgsConstructor
public class ProposalController {



  public final ProposalService proposalServices;
  public final ProposalDiscussionService proposalDiscussionService;




    @GetMapping
    public  ResponseEntity<ApiResponse< List<ProposalSummaryResponse>>> getProposal(@AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
      List<ProposalSummaryResponse> proposalResponse = proposalServices.getAllProposals( tenantId);
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





    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProposalDetailResponse>> getProposalDetail(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        ProposalDetailResponse detail = proposalServices.getProposalDetail(tenantId, id);
        return ResponseEntity.ok(ApiResponse.success("Proposal details fetched successfully", detail));
    }




}
