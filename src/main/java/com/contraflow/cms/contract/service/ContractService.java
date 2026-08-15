package com.contraflow.cms.contract.service;


import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.entity.*;
import com.contraflow.cms.contract.repository.ContractApprovalHistoryRepository;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.mapper.ProposalMapper;
import com.contraflow.cms.proposal.repository.ProposalRepository;
import com.contraflow.cms.security.AuthUser;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.mapper.TenantMapper;
import com.contraflow.cms.tenant.repository.TenantRepository;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

import static com.contraflow.cms.contract.dto.ContractResponse.*;

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


    private String generateContractNumber() {
        long count = contractRepository.count() + 1;
        return "CONTRACT-" + Year.now() + "-" + String.format("%04d", count);
    }


@Transactional
    public String createContract (Long tenantId, Long userId, ContractRequest request){

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

        return "create contract";

  }


     @Transactional
    public List<ContractResponse> getContract(Long tenantId){

        List<Contract>contracts = contractRepository.findByTenantId(tenantId);

        return contracts.stream().map(this::mapToResponse).toList();


    }

    private ContractResponse mapToResponse(Contract contract) {

        Proposal proposal = proposalRepository
                .findById(contract.getProposalId())
                .orElse(null);

        Tenant tenant = tenantRepository.findById(contract.getTenant().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tenant not found with id: " + contract.getTenant().getId()
                        )
                );

        ProposalResponse proposalResponse = null;
        TenantResponse tenantResponse=null;

       if(proposal!=null){
            proposalResponse = proposalMapper.mapToResponse(proposal);
       }
        tenantResponse = tenantMapper.toResponse(tenant);

        return ContractResponse.builder()
                .tenant(tenantResponse)
                .Id(contract.getId())
                .proposal(proposalResponse)
                .contractTitle(contract.getContractTitle())
                .status(contract.getStatus())
                .billingType(String.valueOf(contract.getBillingType()))
                .contractType(String.valueOf(contract.getContractType()))
                .build();
    }
}



