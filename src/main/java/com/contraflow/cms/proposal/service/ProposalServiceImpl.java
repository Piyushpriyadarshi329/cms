package com.contraflow.cms.proposal.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.dto.ClientUserResponse;
import com.contraflow.cms.proposal.dto.ProposalDetailResponse;
import com.contraflow.cms.proposal.dto.ProposalSummaryResponse;
import com.contraflow.cms.proposal.dto.ProposalVersionResponse;
import com.contraflow.cms.proposal.entity.ProposalVersion;
import com.contraflow.cms.proposal.mapper.ProposalMapper;
import com.contraflow.cms.proposal.mapper.ProposalVersionMapper;
import com.contraflow.cms.proposal.repository.ProposalDiscussionRepository;
import com.contraflow.cms.proposal.repository.ProposalVersionRepository;
import com.contraflow.cms.tenant.entity.TenantUser;
import com.contraflow.cms.tenant.mapper.TenantMapper;
import com.contraflow.cms.tenant.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.client.entity.ClientUser;
import com.contraflow.cms.client.repository.ClientRepository;
import com.contraflow.cms.client.repository.ClientUserRepository;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.ProposalLockedException;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalRequest;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.entity.Proposal;
import com.contraflow.cms.proposal.repository.ProposalRepository;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.repository.TenantRepository;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService{
    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientUserRepository clientUserRepository;

    public final ProposalVersionRepository proposalVersionRepository;

    public final TenantUserRepository tenantUserRepository;


    public final ProposalMapper proposalMapper;

    public final ProposalVersionMapper proposalVersionMapper;

    public final TenantMapper tenantMapper;

    public final ContractRepository contractRepository;


    @Transactional
    @Override
    public ProposalResponse createProposal(Long tenantId, ProposalRequest request){

        Tenant tenant=tenantRepository.findById(tenantId).orElseThrow(()-> new ResourceNotFoundException(
            "Tenant not found with id: " + tenantId));

        Client client=clientRepository.findById(request.getClientId()).orElseThrow(() -> new ResourceNotFoundException(
            "Client not found with id: " + request.getClientId()));

        Proposal proposal=new Proposal();
        proposal.setTitle(request.getTitle());
        proposal.setDescription(request.getDescription());
        proposal.setTenant(tenant);
        proposal.setClient(client);
        proposal.setProposalStartDate(request.getProposalStartDate());
        proposal.setProposalNumber(generateProposalNumber());

        if (request.getClientUserId() != null) {
            ClientUser contact = clientUserRepository.findById(request.getClientUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client contact not found with id: " + request.getClientUserId()));
            proposal.setClientUser(contact);
        }
            TenantUser tenantUser = tenantUserRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client contact not found with id: " + request.getClientUserId()));


        Proposal saved = proposalRepository.save(proposal);

        ProposalVersion  proposalVersion = ProposalVersion.builder().tenant(tenant).proposal(proposal).proposalAmount(request.getProposalAmount()).proposalVersionNumber(1).billing(request.getBilling()).startDate(request.getStartDate()).endDate(request.getEndDate()).createdBy(tenantUser).createdAt(LocalDateTime.now()).build();
        proposalVersionRepository.save(proposalVersion);

        return proposalMapper.mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProposalResponse getProposalById(UUID id){
        Proposal proposal=proposalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proposal not found with id: " + id));
        return proposalMapper.mapToResponse(proposal);
    }

    @Override
    @Transactional(readOnly = true)
    public ProposalDetailResponse getProposalDetail(Long tenantId, UUID id) {

        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal not found with id: " + id));

        // multi-tenant isolation: a tenant can only read its own proposals
        if (proposal.getTenant() == null || !proposal.getTenant().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Proposal not found with id: " + id);
        }

        List<ProposalVersionResponse> versions = proposalVersionRepository
                .findByProposalIdOrderByProposalVersionNumberAsc(id)
                .stream()
                .map(proposalVersionMapper::mapToResponse)
                .collect(Collectors.toList());

        return ProposalDetailResponse.builder()
                .id(proposal.getId())
                .proposalNumber(proposal.getProposalNumber())
                .title(proposal.getTitle())
                .description(proposal.getDescription())
                .status(proposal.getStatus())
                .proposalStartDate(proposal.getProposalStartDate())
                .tenant(tenantMapper.toResponse(proposal.getTenant()))
                .client(mapClient(proposal.getClient()))
                .clientUser(mapClientUser(proposal.getClientUser()))
                .discussions(proposalMapper.mapDiscussions(proposal.getId()))
                .versions(versions)
                .build();
    }

    private ClientResponse mapClient(Client client) {
        if (client == null) {
            return null;
        }
        return ClientResponse.builder()
                .id(client.getId())
                .tenantId(client.getTenantId())
                .name(client.getName())
                .email(client.getEmail())
                .mobile(client.getMobile())
                .pan(client.getPan())
                .gst(client.getGst())
                .address(client.getAddress())
                .city(client.getCity())
                .state(client.getState())
                .country(client.getCountry())
                .pincode(client.getPincode())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }

    private ClientUserResponse mapClientUser(ClientUser clientUser) {
        if (clientUser == null) {
            return null;
        }
        return ClientUserResponse.builder()
                .id(clientUser.getId())
                .clientId(clientUser.getClientId())
                .firstname(clientUser.getFirstName())
                .lastname(clientUser.getLastName())
                .mobile(clientUser.getMobile())
                .email(clientUser.getEmail())
                .active(clientUser.getActive())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProposalSummaryResponse> getAllProposals( Long tenantId) {
        return proposalRepository.findByTenantId(tenantId)
                .stream()
                .map(proposalMapper::mapToSummary)
                .collect(Collectors.toList());
    }

    @Override
    public ProposalResponse updateProposal(Long tenantId, UUID id,ProposalRequest request){
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal not found with id: " + id));

        // Once a proposal has an ACTIVE contract, it is locked from further edits.
        // A reverted (soft-deleted) contract does not lock it — the proposal can be revised.
        if (contractRepository.existsByProposalIdAndDeletedFalse(id)) {
            throw new ProposalLockedException(
                    "Proposal cannot be updated because it has already been converted to a contract");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tenant not found with id: " + tenantId));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        proposal.setTitle(request.getTitle());
        proposal.setDescription(request.getDescription());
        proposal.setTenant(tenant);
        proposal.setClient(client);
        proposal.setProposalStartDate(request.getProposalStartDate());

        if (request.getClientUserId() != null) {
            ClientUser contact = clientUserRepository.findById(request.getClientUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client contact not found with id: " + request.getClientUserId()));
            proposal.setClientUser(contact);
        } else {
            proposal.setClientUser(null);
        }

        Proposal updated = proposalRepository.save(proposal);
        return proposalMapper.mapToResponse(updated);
    }

    @Override
    public void deleteProposal(UUID id) {
        if (!proposalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proposal not found with id: " + id);
        }
        proposalRepository.deleteById(id);
    }

    private String generateProposalNumber() {
        long count = proposalRepository.count() + 1;
        return "PROP-" + Year.now() + "-" + String.format("%04d", count);
    }




}
