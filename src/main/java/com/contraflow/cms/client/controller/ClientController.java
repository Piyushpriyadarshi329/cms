package com.contraflow.cms.client.controller;

import com.contraflow.cms.client.dto.ClientResponse;
import com.contraflow.cms.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    public List<ClientResponse> getAllClients(){
        return service.getAllClients();
    }
}
