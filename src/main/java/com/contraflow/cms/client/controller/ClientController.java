package com.contraflow.cms.client.controller;

import com.contraflow.cms.admin.dto.ApiResponse;
import com.contraflow.cms.client.dto.ClientRequest;
import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping("/tenant/{tenantId}/client")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> getAllClients(@PathVariable Long tenantId) {

        List<ClientResponse> clients = service.getAllClients(tenantId);

        return ResponseEntity.ok(
                ApiResponse.success("Clients fetched successfully", clients)
        );
    }

    @GetMapping("client/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> getClientById(
            @PathVariable Long id) {

        ClientResponse client = service.getClientById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Client fetched successfully", client)
        );
    }

    @PostMapping("/tenant/{tenantId}/client")
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(@PathVariable Long tenantId,
            @RequestBody ClientRequest request) {

        ClientResponse client = service.createClient(tenantId,request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client created successfully", client));
    }

    @PutMapping("/tenant/{tenantId}/client/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(
            @PathVariable Long id,
            @PathVariable Long tenantId,
            @RequestBody ClientRequest request) {

        ClientResponse client = service.updateClient(id, tenantId,request);

        return ResponseEntity.ok(
                ApiResponse.success("Client updated successfully", client)
        );
    }

    @DeleteMapping("client/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable Long id) {

        service.deleteClient(id);

        return ResponseEntity.ok(
                ApiResponse.success("Client deleted successfully", null)
        );
    }

}
