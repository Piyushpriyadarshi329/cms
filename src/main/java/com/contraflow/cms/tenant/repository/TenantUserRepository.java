package com.contraflow.cms.tenant.repository;

import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.entity.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenantUserRepository  extends JpaRepository<TenantUser,Long> {
    List<TenantUser> findByTenantId(Long tenantId);
    boolean existsByEmail(String email);
    Optional<TenantUser>findByEmail(String email);

}
