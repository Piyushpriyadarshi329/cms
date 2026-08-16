package com.contraflow.cms.proposal.mapper;

import com.contraflow.cms.proposal.dto.ProposalVersionResponse;
import com.contraflow.cms.proposal.entity.ProposalVersion;
import com.contraflow.cms.tenant.entity.TenantUser;
import org.springframework.stereotype.Component;

@Component
public class ProposalVersionMapper {

    /**
     * Maps a ProposalVersion to a flat DTO. Reading the lazy {@code createdBy} is safe only
     * inside an open session (call this from a @Transactional method).
     */
    public ProposalVersionResponse mapToResponse(ProposalVersion version) {
        TenantUser createdBy = version.getCreatedBy();
        return ProposalVersionResponse.builder()
                .id(version.getId())
                .proposalVersionNumber(version.getProposalVersionNumber())
                .proposalAmount(version.getProposalAmount())
                .currency(version.getCurrency())
                .billing(version.getBilling())
                .startDate(version.getStartDate())
                .endDate(version.getEndDate())
                .createdById(createdBy != null ? createdBy.getId() : null)
                .createdByName(createdBy != null ? (createdBy.getFirstName() + " " + createdBy.getLastName()) : null)
                .createdAt(version.getCreatedAt())
                .build();
    }
}
