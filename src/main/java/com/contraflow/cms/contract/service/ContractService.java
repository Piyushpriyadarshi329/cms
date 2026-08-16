package com.contraflow.cms.contract.service;


import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.dto.TimeLineResponse;
import com.contraflow.cms.contract.entity.*;
import com.contraflow.cms.contract.mapper.TimeLineMapper;
import com.contraflow.cms.contract.repository.ContractApprovalHistoryRepository;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.mapper.ProposalMapper;
import com.contraflow.cms.proposal.repository.ProposalRepository;
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
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

  public final ContractRepository contractRepository;
  public final TenantRepository tenantRepository;
  public final TenantUserRepository tenantUserRepository;
  public final ContractApprovalHistoryRepository contractApprovalHistoryRepository;
  public final ProposalRepository proposalRepository;
  public final ProposalMapper proposalMapper;
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





      Contract contract =  Contract.builder()
              .tenant(tenant)
              .proposalId(request.getProposalId())
              .contractType(ContractType.valueOf(request.getContractType()))
              .billingType(BillingType.valueOf(request.getBillingType()))
              .contractNumber(generateContractNumber())
              .contractTitle(request.getContractTitle()).build();

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


    @Transactional
    public List<ContractResponse> getContract(Long tenantId){

        List<Contract> contracts = contractRepository.findByTenantId(tenantId);
        if (contracts.isEmpty()) {
            return List.of();
        }

        // Every contract in this list belongs to the same tenant — fetch & map it once.
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tenant not found with id: " + tenantId));
        TenantResponse tenantResponse = tenantMapper.toResponse(tenant);

        // Batch-load proposals: one query instead of one per contract.
        List<UUID> proposalIds = contracts.stream()
                .map(Contract::getProposalId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<UUID, Proposal> proposalById = proposalRepository.findAllById(proposalIds).stream()
                .collect(Collectors.toMap(Proposal::getId, Function.identity()));

        // Batch-load approval history: one query instead of one per contract.
        List<UUID> contractIds = contracts.stream().map(Contract::getId).toList();
        Map<UUID, List<ContractApprovalHistory>> historyByContractId =
                contractApprovalHistoryRepository.findByContractIdIn(contractIds).stream()
                        .collect(Collectors.groupingBy(h -> h.getContract().getId()));

        return contracts.stream()
                .map(contract -> mapToResponse(contract, tenantResponse, proposalById, historyByContractId))
                .toList();
    }

    private ContractResponse mapToResponse(Contract contract,
                                           TenantResponse tenantResponse,
                                           Map<UUID, Proposal> proposalById,
                                           Map<UUID, List<ContractApprovalHistory>> historyByContractId) {

        Proposal proposal = proposalById.get(contract.getProposalId());
        ProposalResponse proposalResponse = proposal != null
                ? proposalMapper.mapToResponse(proposal)
                : null;

        List<TimeLineResponse> timeLineResponseList =
                historyByContractId.getOrDefault(contract.getId(), List.of()).stream()
                        .map(timeLineMapper::mapToResponse)
                        .toList();

        return ContractResponse.builder()
                .tenant(tenantResponse)
                .Id(contract.getId())
                .proposal(proposalResponse)
                .contractTitle(contract.getContractTitle())
                .status(contract.getStatus())
                .billingType(String.valueOf(contract.getBillingType()))
                .contractType(String.valueOf(contract.getContractType()))
                .timeLine(timeLineResponseList)
                .build();
    }
}



