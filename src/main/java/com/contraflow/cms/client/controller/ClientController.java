package com.contraflow.cms.client.controller;

import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.client.dto.ClientRequest;
import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.services.ClientService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant")
public class ClientController {

    private final ClientService service;

    @GetMapping("/client")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> getAllClients(
            @AuthenticationPrincipal AuthUser authUser) {

        Long tenantId = authUser.getTenantId();   // from the JWT, not the URL
        List<ClientResponse> clients = service.getAllClients(tenantId);

        return ResponseEntity.ok(
                ApiResponse.success("Clients fetched successfully", clients)
        );
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> getClientById(
            @PathVariable Long id,  @AuthenticationPrincipal AuthUser authUser ) {
            Long tenantId = authUser.getTenantId();   // from the JWT, not the URL
        ClientResponse client = service.getClientById(tenantId,id);

        return ResponseEntity.ok(
                ApiResponse.success("Client fetched successfully", client)
        );
    }

    @PostMapping("/client")
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody ClientRequest request) {

        ClientResponse client = service.createClient(authUser.getTenantId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client created successfully", client));
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(
            @AuthenticationPrincipal AuthUser principal,
            @PathVariable Long id,
            @RequestBody ClientRequest request) {

        ClientResponse client = service.updateClient(id, principal.getTenantId(), request);

        return ResponseEntity.ok(
                ApiResponse.success("Client updated successfully", client)
        );
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable Long id) {

        service.deleteClient(id);

        return ResponseEntity.ok(
                ApiResponse.success("Client deleted successfully", null)
        );
    }

}
