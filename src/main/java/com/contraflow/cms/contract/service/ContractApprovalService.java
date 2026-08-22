package com.contraflow.cms.contract.service;

import com.contraflow.cms.contract.entity.ApprovalAction;
import com.contraflow.cms.contract.entity.Contract;
import com.contraflow.cms.contract.entity.ContractApprovalHistory;
import com.contraflow.cms.contract.entity.ContractStatus;
import com.contraflow.cms.contract.repository.ContractApprovalHistoryRepository;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.InvalidContractStateException;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.signer.service.SignerService;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractApprovalService {

    private final ContractRepository contractRepository;
    private final ContractApprovalHistoryRepository contractApprovalHistoryRepository;
    private final TenantUserRepository tenantUserRepository;
    private final SignerService signerService;

    public void managerApprove(Long tenantId, Long userId, UUID contractId, String comment) {
        transition(tenantId, userId, contractId,
                EnumSet.of(ContractStatus.MANAGER_APPROVAL_PENDING),
                ContractStatus.FINANCE_APPROVAL_PENDING,
                ApprovalAction.APPROVED,
                comment != null ? comment : "Approved by manager");
    }

    public void financeApprove(Long tenantId, Long userId, UUID contractId, String comment) {
        transition(tenantId, userId, contractId,
                EnumSet.of(ContractStatus.FINANCE_APPROVAL_PENDING),
                ContractStatus.LEGAL_APPROVAL_PENDING,
                ApprovalAction.APPROVED,
                comment != null ? comment : "Approved by finance");
    }

    public void legalApprove(Long tenantId, Long userId, UUID contractId, String comment) {
        transition(tenantId, userId, contractId,
                EnumSet.of(ContractStatus.LEGAL_APPROVAL_PENDING),
                ContractStatus.ESIGN_PENDING,
                ApprovalAction.APPROVED,
                comment != null ? comment : "Approved by legal");
        signerService.createRequest(tenantId, contractId);
    }

    public void sendForEsign(Long tenantId, Long userId, UUID contractId, String comment) {
        transition(tenantId, userId, contractId,
                EnumSet.of(ContractStatus.ESIGN_PENDING),
                ContractStatus.PARTIALLY_SIGNED,
                ApprovalAction.SENT_FOR_ESIGN,
                comment != null ? comment : "Sent for e-sign");
    }

    public void close(Long tenantId, Long userId, UUID contractId, String comment) {
        transition(tenantId, userId, contractId,
                EnumSet.of(ContractStatus.PARTIALLY_SIGNED, ContractStatus.ACTIVE),
                ContractStatus.CLOSED,
                ApprovalAction.CLOSED,
                comment != null ? comment : "Contract closed");
    }

    /**
     * Validate the current status, move the contract to {@code newStatus}, and record the
     * action on the approval timeline — all in one transaction.
     */
    @Transactional
    public void transition(Long tenantId,
                           Long userId,
                           UUID contractId,
                           Set<ContractStatus> allowedCurrent,
                           ContractStatus newStatus,
                           ApprovalAction action,
                           String comment) {

        Contract contract = contractRepository.findByIdAndDeletedFalse(contractId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contract not found with id: " + contractId));

        // multi-tenant isolation
        if (contract.getTenant() == null || !contract.getTenant().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Contract not found with id: " + contractId);
        }

        if (!allowedCurrent.contains(contract.getStatus())) {
            throw new InvalidContractStateException(
                    "Contract in status " + contract.getStatus()
                            + " cannot move to " + newStatus);
        }

        TenantUser tenantUser = tenantUserRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        contract.setStatus(newStatus);
        contractRepository.save(contract);

        ContractApprovalHistory history = ContractApprovalHistory.builder()
                .createdAt(LocalDateTime.now())
                .actionAt(LocalDateTime.now())
                .actionBy(tenantUser)
                .action(action)
                .tenant(contract.getTenant())
                .contract(contract)
                .comment(comment)
                .build();
        contractApprovalHistoryRepository.save(history);
    }
}
