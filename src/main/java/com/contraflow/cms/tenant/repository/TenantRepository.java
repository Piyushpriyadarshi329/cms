package com.contraflow.cms.tenant.repository;

import com.contraflow.cms.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant,Long> {

    Optional<Tenant>findByEmail(String email);
    Optional<Tenant>existsByEmail(String email);
}
