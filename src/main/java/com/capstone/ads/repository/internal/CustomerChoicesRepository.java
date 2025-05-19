package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    List<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);

    Optional<CustomerChoices> findFirstByProductType_IdAndUsers_IdOrderByUpdatedAtDesc(String productTypeId, String userId);


}