package com.contraflow.cms.signer.repository;

import com.contraflow.cms.signer.Enum.ESign_Status;
import com.contraflow.cms.signer.entity.Signer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignerRepository extends JpaRepository<Signer,Long> {
     public List<Signer> findByStatus(ESign_Status status);
     public List<Signer> findAllByDeletedFalse();
     Optional <Signer> findByIdAndDeletedFalse(Long id);
     List<Signer> findByTenantIdAndDeletedFalse(Long tenantId);
     Optional<Signer> findByTenantIdAndIdAndDeletedFalse(Long tenantId, Long id);
}
