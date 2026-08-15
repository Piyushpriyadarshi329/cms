package com.contraflow.cms.contract.repository;

import com.contraflow.cms.contract.entity.ContractApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractApprovalHistoryRepository extends JpaRepository<ContractApprovalHistory,Long> {

}
