package com.contraflow.cms.proposal.mapper;

import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalSummaryResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.repository.ProposalDiscussionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Component
@AllArgsConstructor
public class ProposalMapper {

public final ProposalDiscussionRepository proposalDiscussionRepository;


    public ProposalResponse mapToResponse(Proposal proposal) {
        return ProposalResponse.builder()
                .id(proposal.getId())
                .proposalNumber(proposal.getProposalNumber())
                .title(proposal.getTitle())
                .description(proposal.getDescription())
                .tenantId(proposal.getTenant().getId())
                .status(proposal.getStatus())
                .tenantName(proposal.getTenant().getName())
                .clientId(proposal.getClient().getId())
                .clientName(proposal.getClient().getName())
                .clientUserId(proposal.getClientUser() != null ? proposal.getClientUser().getId() : null)
                .proposalStartDate(proposal.getProposalStartDate())
                .proposalDiscussion(mapDiscussions(proposal.getId()))
                .build();
    }


    /**
     * List view: proposal columns + FK ids only. Reading {@code getTenant().getId()} on a
     * LAZY association returns the FK without initializing the proxy, so this issues no
     * extra queries per proposal (no name lookups, no discussion query).
     */
    public ProposalSummaryResponse mapToSummary(Proposal proposal) {
        return ProposalSummaryResponse.builder()
                .id(proposal.getId())
                .proposalNumber(proposal.getProposalNumber())
                .title(proposal.getTitle())
                .description(proposal.getDescription())
                .status(proposal.getStatus())
                .proposalStartDate(proposal.getProposalStartDate())
                .tenantId(proposal.getTenant() != null ? proposal.getTenant().getId() : null)
                .clientId(proposal.getClient() != null ? proposal.getClient().getId() : null)
                .clientUserId(proposal.getClientUser() != null ? proposal.getClientUser().getId() : null)
                .build();
    }


    public List<ProposalDiscussionResponse> mapDiscussions(UUID proposalId) {
        return proposalDiscussionRepository.findByProposalId(proposalId)
                .stream()
                .map(d -> ProposalDiscussionResponse.builder()
                        .id(d.getId())
                        // .getId() on a lazy proxy does NOT initialize it — safe outside a session.
                        .proposalId(d.getProposal() != null ? d.getProposal().getId() : null)
                        .tenantUserId(d.getTenantUser() != null ? d.getTenantUser().getId() : null)
                        .clientUserId(d.getClientUser() != null ? d.getClientUser().getId() : null)
                        .meetingDate(d.getMeetingDate())
                        .title(d.getTitle())
                        .description(d.getDescription())
                        .remarks(d.getRemarks())
                        .requirement(d.getRequirement())
                        .build())
                .collect(Collectors.toList());
    }




}
