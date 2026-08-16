package com.contraflow.cms.proposal.repository;

import com.contraflow.cms.proposal.entity.ProposalVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProposalVersionRepository extends JpaRepository<ProposalVersion, Long> {

    Optional<ProposalVersion> findTopByProposalIdOrderByProposalVersionNumberDesc(UUID proposalId);

    List<ProposalVersion> findByProposalIdOrderByProposalVersionNumberAsc(UUID proposalId);

}
