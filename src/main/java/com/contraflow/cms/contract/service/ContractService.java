package com.contraflow.cms.contract.service;


import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.dto.ContractRevertRequest;
import com.contraflow.cms.contract.dto.ContractSummaryResponse;
import com.contraflow.cms.contract.dto.TimeLineResponse;
import com.contraflow.cms.contract.entity.*;
import com.contraflow.cms.contract.mapper.TimeLineMapper;
import com.contraflow.cms.contract.repository.ContractApprovalHistoryRepository;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.DuplicateResourceException;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalVersionResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.entity.ProposalVersion;
import com.contraflow.cms.proposal.mapper.ProposalMapper;
import com.contraflow.cms.proposal.mapper.ProposalVersionMapper;
import com.contraflow.cms.proposal.repository.ProposalRepository;
import com.contraflow.cms.proposal.repository.ProposalVersionRepository;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.mapper.TenantMapper;
import com.contraflow.cms.tenant.repository.TenantRepository;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

  public final ContractRepository contractRepository;
  public final TenantRepository tenantRepository;
  public final TenantUserRepository tenantUserRepository;
  public final ContractApprovalHistoryRepository contractApprovalHistoryRepository;
  public final ProposalRepository proposalRepository;
  public final ProposalVersionRepository proposalVersionRepository;
  public final ProposalMapper proposalMapper;
  public final ProposalVersionMapper proposalVersionMapper;
  public final TenantMapper tenantMapper;
  public final TimeLineMapper timeLineMapper;


    private String generateContractNumber() {
        long count = contractRepository.count() + 1;
        return "CONTRACT-" + Year.now() + "-" + String.format("%04d", count);
    }


    @Transactional
    public void createContract (Long tenantId, Long userId, ContractRequest request){

      Tenant tenant = tenantRepository.findById(tenantId)
              .orElseThrow(() ->
                      new RuntimeException("Tenant not found with id: " + tenantId)
              );

        TenantUser tenantUser = tenantUserRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId)
                );


    Proposal proposal = proposalRepository.findById(request.getProposalId()).orElseThrow(()->
            new RuntimeException("Proposal Not found: "+ request.getProposalId())
            );

        // At most one ACTIVE contract per proposal (reverted ones are soft-deleted and don't count).
        if (contractRepository.existsByProposalIdAndDeletedFalse(proposal.getId())) {
            throw new DuplicateResourceException(
                    "An active contract already exists for proposal: " + proposal.getId());
        }

        // Snapshot the latest proposal version onto the contract at conversion time.
        ProposalVersion latestVersion = proposalVersionRepository
                .findTopByProposalIdOrderByProposalVersionNumberDesc(proposal.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No proposal version found for proposal: " + proposal.getId()));

      Contract contract =  Contract.builder()
              .tenant(tenant)
              .proposalId(request.getProposalId())
              .contractType(ContractType.valueOf(request.getContractType()))
              .contractNumber(generateContractNumber())
              .contractTitle(request.getContractTitle())
              .proposalAmount(latestVersion.getProposalAmount())
              .currency(latestVersion.getCurrency())
              .billing(latestVersion.getBilling())
              .startDate(latestVersion.getStartDate())
              .endDate(latestVersion.getEndDate())
              .proposalVersionNumber(latestVersion.getProposalVersionNumber())
              .build();

        Contract saveContract= contractRepository.save(contract);


        ContractApprovalHistory contractApprovalHistory= ContractApprovalHistory.builder()
                .createdAt(LocalDateTime.now())
                .actionAt(LocalDateTime.now())
                .actionBy(tenantUser)
                .action(ApprovalAction.CREATED)
                .tenant(tenant)
                .contract(saveContract)
                .comment("-")
                .build();

    proposal.setStatus(Proposal.ProposalStatus.COMPLETE);
    proposalRepository.save(proposal);

        contractApprovalHistoryRepository.save(contractApprovalHistory);

}


    /**
     * List view: contract columns + FK ids only. Reading {@code getTenant().getId()} on a
     * LAZY association returns the FK without initializing the proxy, so the whole list is
     * a single query. Full tenant/proposal/timeline data comes from {@link #getContractDetail}.
     */
    @Transactional
    public List<ContractSummaryResponse> getContract(Long tenantId){
        return contractRepository.findByTenantIdAndDeletedFalse(tenantId).stream()
                .map(this::mapToSummary)
                .toList();
    }

    private ContractSummaryResponse mapToSummary(Contract contract) {
        return ContractSummaryResponse.builder()
                .id(contract.getId())
                .contractNumber(contract.getContractNumber())
                .contractTitle(contract.getContractTitle())
                .status(contract.getStatus())
                .contractType(String.valueOf(contract.getContractType()))
                .proposalId(contract.getProposalId())
                .tenantId(contract.getTenant() != null ? contract.getTenant().getId() : null)
                .build();
    }

    /**
     * Detail view: full contract with tenant, proposal and approval timeline.
     */
    @Transactional
    public ContractResponse getContractDetail(Long tenantId, UUID contractId){

        Contract contract = contractRepository.findByIdAndDeletedFalse(contractId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contract not found with id: " + contractId));

        // multi-tenant isolation: a tenant can only read its own contracts
        if (contract.getTenant() == null || !contract.getTenant().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Contract not found with id: " + contractId);
        }

        TenantResponse tenantResponse = tenantMapper.toResponse(contract.getTenant());

        Proposal proposal = contract.getProposalId() != null
                ? proposalRepository.findById(contract.getProposalId()).orElse(null)
                : null;
        ProposalResponse proposalResponse = proposal != null
                ? proposalMapper.mapToResponse(proposal)
                : null;

        // Timeline in chronological order (shaped for the shared frontend timeline component).
        List<TimeLineResponse> timeLineResponseList =
                contractApprovalHistoryRepository.findByContractIdOrderByActionAtAsc(contract.getId()).stream()
                        .map(timeLineMapper::mapToResponse)
                        .toList();

        List<ProposalVersionResponse> proposalVersions = contract.getProposalId() != null
                ? proposalVersionRepository
                        .findByProposalIdOrderByProposalVersionNumberAsc(contract.getProposalId()).stream()
                        .map(proposalVersionMapper::mapToResponse)
                        .toList()
                : List.of();

        return ContractResponse.builder()
                .Id(contract.getId())
                .tenant(tenantResponse)
                .proposal(proposalResponse)
                .contractTitle(contract.getContractTitle())
                .status(contract.getStatus())
                .contractType(String.valueOf(contract.getContractType()))
                .proposalAmount(contract.getProposalAmount())
                .currency(contract.getCurrency())
                .billing(contract.getBilling())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .proposalVersionNumber(contract.getProposalVersionNumber())
                .timeLine(timeLineResponseList)
                .proposalVersions(proposalVersions)
                .build();
    }

    /**
     * Revert a contract back to its proposal (used when finance/legal send it back).
     * The contract is soft-deleted and the proposal is reopened as REVISED, so a new
     * discussion + version can be added and a fresh contract created later.
     */
    @Transactional
    public void revertToProposal(Long tenantId, Long userId, UUID contractId, ContractRevertRequest request){

        Contract contract = contractRepository.findByIdAndDeletedFalse(contractId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contract not found with id: " + contractId));

        // multi-tenant isolation
        if (contract.getTenant() == null || !contract.getTenant().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Contract not found with id: " + contractId);
        }

        TenantUser tenantUser = tenantUserRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        // soft-delete + cancel the contract
        contract.setDeleted(true);
        contract.setStatus(ContractStatus.CANCELLED);
        contractRepository.save(contract);

        // reopen the proposal for revision
        Proposal proposal = proposalRepository.findById(contract.getProposalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal not found with id: " + contract.getProposalId()));
        proposal.setStatus(Proposal.ProposalStatus.REVISED);
        proposalRepository.save(proposal);

        // record the revert on the timeline
        ContractApprovalHistory history = ContractApprovalHistory.builder()
                .createdAt(LocalDateTime.now())
                .actionAt(LocalDateTime.now())
                .actionBy(tenantUser)
                .action(ApprovalAction.REVERTED)
                .tenant(contract.getTenant())
                .contract(contract)
                .comment(request != null && request.getComment() != null ? request.getComment() : "-")
                .build();
        contractApprovalHistoryRepository.save(history);
    }
}



