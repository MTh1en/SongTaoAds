package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Contractors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContractorsRepository extends JpaRepository<Contractors, String> {
    Optional<Contractors> findByIdAndIsAvailable(String id, Boolean isAvailable);

    Page<Contractors> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    Page<Contractors> findByIsInternalAndIsAvailable(Boolean isInternal, Boolean isAvailable, Pageable pageable);
}