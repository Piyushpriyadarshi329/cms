package com.contraflow.cms.contract.repository;

import com.contraflow.cms.contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository extends JpaRepository <Contract , UUID>{

    List<Contract> findByTenantIdAndDeletedFalse(Long tenantId);

    Optional<Contract> findByIdAndDeletedFalse(UUID id);

    // Only ACTIVE contracts count — a reverted (soft-deleted) one must not lock the proposal.
    boolean existsByProposalIdAndDeletedFalse(UUID proposalId);
}

