package com.contraflow.cms.client.controller;

import com.contraflow.cms.admin.dto.ApiResponse;
import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.dto.ClientUserRequest;
import com.contraflow.cms.client.dto.ClientUserResponse;
import com.contraflow.cms.client.services.ClientUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ClientUserController {
    private ClientUserService service;

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ClientUserResponse>>> getAllClientUsers(@PathVariable Long clientId){

        List<ClientUserResponse> users = service.getClientUsers(clientId);

        return ResponseEntity.ok(
                ApiResponse.success("client's user fetched successfully", users)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientUserResponse>> getUserById(@PathVariable Long id){
        ClientUserResponse user = service.getClientUserById(id);

        return ResponseEntity.ok(
                ApiResponse.success("user fetched successfully", user)
        );
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ClientUserResponse>> createClientUser(@RequestBody ClientUserRequest request){

        ClientUserResponse user = service.createClientUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("user created successfully", user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientUserResponse>> updateClientUser(@PathVariable Long id, @RequestBody ClientUserRequest request){

        ClientUserResponse user = service.updateClientUser(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClientUser(@PathVariable Long id){
        service.deleteClientUser(id);

        return ResponseEntity.ok(
                ApiResponse.success("user deleted successfully", null)
        );
    }


}
