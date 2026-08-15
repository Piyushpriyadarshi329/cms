package com.contraflow.cms.contract.service;


import com.contraflow.cms.contract.dto.ContractRequest;
import com.contraflow.cms.contract.entity.BillingType;
import com.contraflow.cms.contract.entity.Contract;
import com.contraflow.cms.contract.entity.ContractType;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.repository.TenantRepository;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {

  public final ContractRepository contractRepository;
  public final TenantRepository tenantRepository;


  public String createContract (Long tenantId, ContractRequest request){

      Tenant tenant = tenantRepository.findById(tenantId)
              .orElseThrow(() ->
                      new RuntimeException("Tenant not found with id: " + tenantId)
              );

      Contract contract =  Contract.builder()
              .tenant(tenant)
              .proposalId(request.getProposalId())
              .contractType(ContractType.valueOf(request.getContractType()))
              .billingType(BillingType.valueOf(request.getBillingType()))
              .contractTitle(request.getContractTitle()).status(request.getStatus()).build();
              contractRepository.save(contract);



      return "create contract";


  }





}
