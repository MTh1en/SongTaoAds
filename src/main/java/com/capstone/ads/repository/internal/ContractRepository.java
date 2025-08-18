package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Contract;
import com.capstone.ads.model.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    Optional<Contract> findByOrders_Id(String id);

    int countByStatus(ContractStatus status);
}