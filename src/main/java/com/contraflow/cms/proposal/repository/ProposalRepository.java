package com.contraflow.cms.proposal.repository;

import com.contraflow.cms.proposal.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProposalRepository  extends JpaRepository<Proposal, UUID> {
}
