package com.contraflow.cms.proposal.service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import com.contraflow.cms.proposal.mapper.ProposalMapper;
import com.contraflow.cms.proposal.repository.ProposalDiscussionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.client.entity.ClientUser;
import com.contraflow.cms.client.repository.ClientRepository;
import com.contraflow.cms.client.repository.ClientUserRepository;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
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
    @Autowired
    private ProposalDiscussionRepository proposalDiscussionRepository;


    public final ProposalMapper proposalMapper;

    @Override
    public ProposalResponse createProposal(Long tenantId, ProposalRequest request){

        Tenant tenant=tenantRepository.findById(tenantId).orElseThrow(()-> new ResourceNotFoundException(
            "Tenant not found with id: " + request.getTenantId()));

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

        Proposal saved = proposalRepository.save(proposal);

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
    public List<ProposalResponse> getAllProposals( Long tenantId) {
        return proposalRepository.findByTenantId(tenantId)
                .stream()
                .map(proposalMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProposalResponse updateProposal(UUID id,ProposalRequest request){
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal not found with id: " + id));

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tenant not found with id: " + request.getTenantId()));

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
