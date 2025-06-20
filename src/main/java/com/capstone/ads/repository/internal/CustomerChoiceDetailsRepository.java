package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerChoiceDetailsRepository extends JpaRepository<CustomerChoiceDetails, String> {
    List<CustomerChoiceDetails> findByCustomerChoices_IdOrderByCreatedAtAsc(String id);

    Optional<CustomerChoiceDetails> findByCustomerChoices_IdAndAttributeValues_Id(String id, String id1);
}