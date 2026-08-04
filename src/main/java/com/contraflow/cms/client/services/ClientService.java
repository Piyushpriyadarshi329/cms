package com.contraflow.cms.client.services;

import com.contraflow.cms.client.dto.ClientRequest;
import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    public final ClientRepository repository;

    // get client
    public List<ClientResponse> getAllClients(Long tenantId){
        return repository.findBytenantId(tenantId)
                .stream()
                .map(client -> new ClientResponse(
                        client.getId(),
                        client.getTenantId(),
                        client.getName(),
                        client.getEmail(),
                        client.getMobile(),
                        client.getPan(),
                        client.getGst(),
                        client.getAddress(),
                        client.getCity(),
                        client.getState(),
                        client.getCountry(),
                        client.getPincode(),
                        client.getCreatedAt(),
                        client.getUpdatedAt()
                ))
                .toList();

    }

    // create
    public ClientResponse createClient(ClientRequest request){
        Client client = Client.builder()
                .tenantId(request.getTenantId())
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .pan(request.getPan())
                .gst(request.getGst())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .state(request.getState())
                .pincode(request.getPincode())
                .build();

        Client savedClient = repository.save(client);

        return new ClientResponse(
                savedClient.getId(),
                savedClient.getTenantId(),
                savedClient.getName(),
                savedClient.getEmail(),
                savedClient.getMobile(),
                savedClient.getPan(),
                savedClient.getGst(),
                savedClient.getAddress(),
                savedClient.getCity(),
                savedClient.getState(),
                savedClient.getCountry(),
                savedClient.getPincode(),
                savedClient.getCreatedAt(),
                savedClient.getUpdatedAt()
        );
    }


    // Update Client
    public ClientResponse updateClient(Long id, ClientRequest request) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id : " + id));

        client.setTenantId(request.getTenantId());
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setMobile(request.getMobile());
        client.setPan(request.getPan());
        client.setGst(request.getGst());
        client.setAddress(request.getAddress());
        client.setCity(request.getCity());
        client.setState(request.getState());
        client.setCountry(request.getCountry());
        client.setPincode(request.getPincode());

        Client updatedClient = repository.save(client);

        return new ClientResponse(
                updatedClient.getId(),
                updatedClient.getTenantId(),
                updatedClient.getName(),
                updatedClient.getEmail(),
                updatedClient.getMobile(),
                updatedClient.getPan(),
                updatedClient.getGst(),
                updatedClient.getAddress(),
                updatedClient.getCity(),
                updatedClient.getState(),
                updatedClient.getCountry(),
                updatedClient.getPincode(),
                updatedClient.getCreatedAt(),
                updatedClient.getUpdatedAt()
        );
    }

    // Delete Client
    public String deleteClient(Long id) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id : " + id));

        repository.delete(client);

        return "Client deleted successfully.";
    }

    // Get Client By Id
    public ClientResponse getClientById(Long id) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id : " + id));

        return new ClientResponse(
                client.getId(),
                client.getTenantId(),
                client.getName(),
                client.getEmail(),
                client.getMobile(),
                client.getPan(),
                client.getGst(),
                client.getAddress(),
                client.getCity(),
                client.getState(),
                client.getCountry(),
                client.getPincode(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}
