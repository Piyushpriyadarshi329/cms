package com.contraflow.cms.proposal.service;


import java.util.List;
import java.util.UUID;


import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;


public interface ProposalService {
    ProposalResponse createProposal(Long tenantId, ProposalRequest request);

    ProposalResponse getProposalById(UUID id);

    List<ProposalResponse> getAllProposals( Long tenantId);

    ProposalResponse updateProposal(UUID id, ProposalRequest request);

    void deleteProposal(UUID id);
    
} 
