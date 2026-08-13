package com.contraflow.cms.proposal.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contraflow.cms.client.entity.ClientUser;
import com.contraflow.cms.client.repository.ClientUserRepository;
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

    @Autowired
    private ClientUserRepository clientUserRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public ProposalDiscussionResponse addDiscussion(Long tenantId, ProposalDiscussionRequest request) {

        Proposal proposal = proposalRepository.findById(request.getProposalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal not found with id: " + request.getProposalId()));

        TenantUser tenantUser = tenantUserRepository.findById(request.getTenantUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tenant user not found with id: " + request.getTenantUserId()));

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tenant not found with id: " + tenantId));

        ProposalDiscussion discussion = new ProposalDiscussion();
        discussion.setProposal(proposal);
        discussion.setTenantUser(tenantUser);
        discussion.setMeetingDate(request.getMeetingDate());
        discussion.setTitle(request.getTitle());
        discussion.setDescription(request.getDescription());
        discussion.setRemarks(request.getRemarks());
        discussion.setRequirement(request.getRequirement());
        discussion.setTenant(tenant);

        if (request.getClientUserId() != null) {
            ClientUser contact = clientUserRepository.findById(request.getClientUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client user not found with id: " + request.getClientUserId()));
            discussion.setClientUser(contact);
        }

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
        response.setClientUserId(
                discussion.getClientUser() != null ? discussion.getClientUser().getId() : null);
        response.setMeetingDate(discussion.getMeetingDate());
        response.setTitle(discussion.getTitle());
        response.setDescription(discussion.getDescription());
        response.setRemarks(discussion.getRemarks());
        response.setRequirement(discussion.getRequirement());
        return response;
    }
}