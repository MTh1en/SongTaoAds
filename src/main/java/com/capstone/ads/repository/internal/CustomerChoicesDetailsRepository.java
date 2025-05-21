package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoicesDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerChoicesDetailsRepository extends JpaRepository<CustomerChoicesDetails, String> {
    List<CustomerChoicesDetails> findByCustomerChoices_IdOrderByCreatedAtAsc(String id);

    Optional<CustomerChoicesDetails> findByCustomerChoices_IdAndAttributeValues_Id(String id, String id1);
}