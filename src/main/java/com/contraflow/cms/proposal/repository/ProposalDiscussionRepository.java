package com.contraflow.cms.proposal.repository;

import com.contraflow.cms.proposal.entity.ProposalDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProposalDiscussionRepository extends JpaRepository<ProposalDiscussion, UUID> {
    List<ProposalDiscussion> findByProposalId(UUID proposalId);
    List<ProposalDiscussion> findByTenantUserId(Long tenantUserId);
    List<ProposalDiscussion> findByClientUserId(Long clientUserId);

}