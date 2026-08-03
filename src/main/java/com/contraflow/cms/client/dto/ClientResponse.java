package com.contraflow.cms.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ClientResponse {
    private Long id;

    private Long tenantId;

    private String name;

    private String email;

    private String mobile;

    private String pan;

    private String gst;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
