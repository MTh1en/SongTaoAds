package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, String> {
    Optional<Contract> findByOrders_Id(String id);
}