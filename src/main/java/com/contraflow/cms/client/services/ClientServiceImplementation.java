package com.contraflow.cms.client.services;

import com.contraflow.cms.client.dto.ClientRequest;
import com.contraflow.cms.client.dto.ClientResponse;

import java.util.List;

public interface ClientServiceImplementation {
//    ClientResponse createClient(ClientRequest request);
//    ClientResponse getClientById(Long id);
    List<ClientResponse> getAllClients();
//    ClientResponse updateClient(Long id, ClientRequest request);
//    void deleteClient(Long id);
}
