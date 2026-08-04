package com.contraflow.cms.proposal.repository;

import com.contraflow.cms.proposal.entity.Proposal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProposalRepository  extends JpaRepository<Proposal, UUID> {

    List<Proposal> findByTenantId(Long tenantId);
    List<Proposal> findByClientId(Long clientId);
    Optional<Proposal> findByProposalNumber(String proposalNumber);
    
}
