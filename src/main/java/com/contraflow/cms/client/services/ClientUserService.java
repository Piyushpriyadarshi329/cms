package com.contraflow.cms.client.services;

import com.contraflow.cms.client.dto.ClientUserRequest;
import com.contraflow.cms.client.dto.ClientUserResponse;
import com.contraflow.cms.client.entity.ClientUser;
import com.contraflow.cms.client.repository.ClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientUserService {

    public final ClientUserRepository repository;

    public List<ClientUserResponse> getClientUsers(Long tenantId,  Long clientId){

        System.out.println("t"+tenantId+"C"+clientId);
        return repository.findByTenantIdAndClientId(tenantId, clientId)
                .stream()
                .map(user -> new ClientUserResponse(
                        user.getId(),
                        user.getClientId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getMobile(),
                        user.getEmail(),
                        user.getActive()
                )).toList();
    }

    // create
    public ClientUserResponse createClientUser(Long tenantId, Long clientId, ClientUserRequest request){
        ClientUser clientUser = ClientUser.builder()
                .clientId(clientId)
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .tenantId(tenantId)
                .build();

        ClientUser savedClientUser = repository.save(clientUser);

        return new ClientUserResponse(
                savedClientUser.getId(),
                savedClientUser.getClientId(),
                savedClientUser.getFirstName(),
                savedClientUser.getLastName(),
                savedClientUser.getEmail(),
                savedClientUser.getMobile(),
                savedClientUser.getActive()
        );
    }

    // update client user
    public ClientUserResponse updateClientUser(Long id, Long clientId, ClientUserRequest request){
        ClientUser clientUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client User not found with id : " + id));

        clientUser.setClientId(clientId);
        clientUser.setFirstName(request.getFirstname());
        clientUser.setLastName(request.getLastname());
        clientUser.setEmail(request.getEmail());
        clientUser.setMobile(request.getMobile());
        clientUser.setActive(request.getActive());

        ClientUser updatedClientUser = repository.save(clientUser);

        return new ClientUserResponse(
                updatedClientUser.getId(),
                updatedClientUser.getClientId(),
                updatedClientUser.getFirstName(),
                updatedClientUser.getLastName(),
                updatedClientUser.getMobile(),
                updatedClientUser.getEmail(),
                updatedClientUser.getActive()
        );
    }

    // delete client user
    public String deleteClientUser(Long id){
        ClientUser clientUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client User not found with id : " + id));

        repository.delete(clientUser);

        return "Client user delete successfully";
    }

    // get client user by id
    public ClientUserResponse getClientUserById(Long id){
        ClientUser clientUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client user not found with id : "+ id));

        return new ClientUserResponse(
                clientUser.getId(),
                clientUser.getClientId(),
                clientUser.getFirstName(),
                clientUser.getLastName(),
                clientUser.getMobile(),
                clientUser.getEmail(),
                clientUser.getActive()
        );
    }

}
