package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerChoiceDetailsRepository extends JpaRepository<CustomerChoiceDetails, String> {
    List<CustomerChoiceDetails> findByCustomerChoices_IdOrderByCreatedAtAsc(String id);
}