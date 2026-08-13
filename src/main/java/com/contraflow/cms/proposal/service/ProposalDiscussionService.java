package com.contraflow.cms.proposal.service;

import java.util.List;
import java.util.UUID;

import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;


public interface ProposalDiscussionService {

    ProposalDiscussionResponse addDiscussion(Long tenantId, ProposalDiscussionRequest request);

    List<ProposalDiscussionResponse> getDiscussionsByProposalId(UUID proposalId);


    
    
} 