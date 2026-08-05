package com.contraflow.cms.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contraflow.cms.client.entity.ClientUser;

import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser,Long>{


    Optional<ClientUser> findByClientId(Long clientId);

    
} 
