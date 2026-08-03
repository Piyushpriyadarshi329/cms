package com.contraflow.cms.client.services;

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

    public List<ClientResponse> getAllClients(){
        return repository.findAll().stream().map(client->new ClientResponse(
                client.getId(),
                client.getTenantId(),
                client.getName(

                ),
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
                )).toList();
    }


}
