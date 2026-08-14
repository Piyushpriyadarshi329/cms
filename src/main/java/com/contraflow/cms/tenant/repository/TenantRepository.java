package com.contraflow.cms.tenant.repository;

import com.contraflow.cms.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant,Long> {

    Optional<Tenant>findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    List<Tenant> findAllByDeletedFalse();
    Optional<Tenant> findByIdAndDeletedFalse(Long id);
    Optional<Tenant> findByEmailAndDeletedFalse(String email);
}
