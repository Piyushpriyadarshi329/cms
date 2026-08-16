package com.contraflow.cms.proposal.service;


import java.util.List;
import java.util.UUID;


import com.contraflow.cms.proposal.dto.ProposalDetailResponse;
import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalSummaryResponse;


public interface ProposalService {
    ProposalResponse createProposal(Long tenantId, ProposalRequest request);

    ProposalResponse getProposalById(UUID id);

    ProposalDetailResponse getProposalDetail(Long tenantId, UUID id);

    List<ProposalSummaryResponse> getAllProposals( Long tenantId);

    ProposalResponse updateProposal(Long tenantId, UUID id, ProposalRequest request);

    void deleteProposal(UUID id);
    
} 
