package com.contraflow.cms.client.controller;

import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.dto.ClientUserRequest;
import com.contraflow.cms.client.dto.ClientUserResponse;
import com.contraflow.cms.client.services.ClientUserService;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tenant/client")
@RequiredArgsConstructor
public class ClientUserController {
    private final ClientUserService service;

    @GetMapping("/{clientId}/user")
    public ResponseEntity<ApiResponse<List<ClientUserResponse>>> getAllClientUsers(@PathVariable Long clientId , @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        List<ClientUserResponse> users = service.getClientUsers(tenantId,clientId);
        return ResponseEntity.ok(
                ApiResponse.success("client's user fetched successfully", users)
        );
    }


    @GetMapping("/{clientId}/user/{id}")
    public ResponseEntity<ApiResponse<ClientUserResponse>> getUserById(@PathVariable Long id){
        ClientUserResponse user = service.getClientUserById(id);

        return ResponseEntity.ok(
                ApiResponse.success("user fetched successfully", user)
        );
    }


    @PostMapping("/{clientId}/user")
    public ResponseEntity<ApiResponse<ClientUserResponse>> createClientUser(@PathVariable Long clientId, @RequestBody ClientUserRequest request, @AuthenticationPrincipal AuthUser authUser){
        Long tenantId = authUser.getTenantId();
        ClientUserResponse user = service.createClientUser(tenantId,clientId,request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("user created successfully", user));
    }


    @PutMapping("/{clientId}/user/{id}")
    public ResponseEntity<ApiResponse<ClientUserResponse>> updateClientUser(@PathVariable Long id,@PathVariable Long clientId,   @RequestBody ClientUserRequest request){

        ClientUserResponse user = service.updateClientUser(id,clientId, request);

        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", user)
        );
    }

    @DeleteMapping("/{clientId}/user/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClientUser(@PathVariable Long id){
        service.deleteClientUser(id);

        return ResponseEntity.ok(
                ApiResponse.success("user deleted successfully", null)
        );
    }


}
