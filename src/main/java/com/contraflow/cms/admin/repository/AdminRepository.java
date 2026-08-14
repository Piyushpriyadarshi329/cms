package com.contraflow.cms.admin.repository;

import com.contraflow.cms.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    List<Admin> findAllByDeletedFalse();

    Optional<Admin> findByIdAndDeletedFalse(Long id);

    Optional<Admin> findByEmailAndDeletedFalse(String email);

}
