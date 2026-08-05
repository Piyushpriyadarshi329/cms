package com.contraflow.cms.contract.repository;

import com.contraflow.cms.contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractRepository extends JpaRepository <Contract , UUID>{
}

