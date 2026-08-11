package com.contraflow.cms.client.repository;


import com.contraflow.cms.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
//    Optional<Client> findByEmail(String email);
        List<Client> findBytenantId(Long tenantId);
        List<Client> findByIdAndTenantId(
                Long tenantId,
                Long clientId
        );
//    Optional<Client> findByMobile(String mobile);
//    Optional<Client> findByPan(String pan);
//    boolean existsByEmail(String email);
//    boolean existsByMobile(String mobile);
//    boolean existsByPan(String pan);
}
