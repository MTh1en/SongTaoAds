package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    Optional<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);

    Optional<CustomerChoices> findByProductTypes_IdAndUsers_Id(String productTypeId, String userId);
}