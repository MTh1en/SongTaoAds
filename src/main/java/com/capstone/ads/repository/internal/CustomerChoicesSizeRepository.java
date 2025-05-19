package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoicesSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerChoicesSizeRepository extends JpaRepository<CustomerChoicesSize, String> {
    Optional<CustomerChoicesSize> findByCustomerChoices_IdAndSize_Id(String id, String id1);

    List<CustomerChoicesSize> findByCustomerChoices_Id(String id);
}