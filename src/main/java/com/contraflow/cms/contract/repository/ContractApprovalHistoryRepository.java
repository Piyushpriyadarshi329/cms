package com.contraflow.cms.contract.repository;

import com.contraflow.cms.contract.entity.ContractApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContractApprovalHistoryRepository extends JpaRepository<ContractApprovalHistory,Long> {
    List<ContractApprovalHistory> findByContractId(UUID ContractId);

    // Ordered chronologically so the timeline can pair consecutive events to compute TAT.
    List<ContractApprovalHistory> findByContractIdOrderByActionAtAsc(UUID contractId);

    List<ContractApprovalHistory> findByContractIdIn(List<UUID> contractIds);

}
