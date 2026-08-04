package com.contraflow.cms.proposal.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.entity.ProposalDiscussion;
import com.contraflow.cms.proposal.repository.ProposalDiscussionRepository;
import com.contraflow.cms.proposal.repository.ProposalRepository;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;

@Service
public class ProposalDiscussionServiceImpl implements ProposalDiscussionService {

    @Autowired
    private ProposalDiscussionRepository proposalDiscussionRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private TenantUserRepository tenantUserRepository;

    // @Autowired
    // private ClientContactRepository clientContactRepository;


    @Override
    public ProposalDiscussionResponse addDiscussion(ProposalDiscussionRequest request) {

        Proposal proposal = proposalRepository.findById(request.getProposalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal not found with id: " + request.getProposalId()));

        TenantUser tenantUser = tenantUserRepository.findById(request.getTenantUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tenant user not found with id: " + request.getTenantUserId()));

        ProposalDiscussion discussion = new ProposalDiscussion();
        discussion.setProposal(proposal);
        // discussion.setTenantUser(tenantUser);
        discussion.setMeetingDate(request.getMeetingDate());
        discussion.setTitle(request.getTitle());
        discussion.setDescription(request.getDescription());
        discussion.setRemarks(request.getRemarks());
        discussion.setRequirement(request.getRequirement());

        // if (request.getClientContactId() != null) {
        //     ClientContact contact = clientContactRepository.findById(request.getClientContactId())
        //             .orElseThrow(() -> new ResourceNotFoundException(
        //                     "Client contact not found with id: " + request.getClientContactId()));
        //     discussion.setClientContact(contact);
        // }

        ProposalDiscussion saved = proposalDiscussionRepository.save(discussion);
        return mapToResponse(saved);
    }

    @Override
    public List<ProposalDiscussionResponse> getDiscussionsByProposalId(UUID proposalId) {
        List<ProposalDiscussion> discussions = proposalDiscussionRepository.findByProposalId(proposalId);
        return discussions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProposalDiscussionResponse mapToResponse(ProposalDiscussion discussion) {
        ProposalDiscussionResponse response = new ProposalDiscussionResponse();
        response.setId(discussion.getId());
        response.setProposalId(discussion.getProposal().getId());
        response.setTenantUserId(discussion.getTenantUser().getId());
        // response.setClientContactId(
                // discussion.getClientContact() != null ? discussion.getClientContact().getId() : null);
        response.setMeetingDate(discussion.getMeetingDate());
        response.setTitle(discussion.getTitle());
        response.setDescription(discussion.getDescription());
        response.setRemarks(discussion.getRemarks());
        response.setRequirement(discussion.getRequirement());
        return response;
    }
}
