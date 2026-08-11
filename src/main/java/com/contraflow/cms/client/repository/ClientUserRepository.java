package com.contraflow.cms.client.repository;

import com.contraflow.cms.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import com.contraflow.cms.client.entity.ClientUser;

import java.util.List;
import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser,Long>{


    Optional<ClientUser> findByClientId( Long clientId);

    List<ClientUser> findByTenantIdAndClientId(
            Long tenantId,
            Long clientId
    );

    
} 
