package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoicesSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerChoicesSizeRepository extends JpaRepository<CustomerChoicesSize, String> {
    boolean existsByCustomerChoices_IdAndSize_Id(String customerChoiceId, String sizeId);

    List<CustomerChoicesSize> findByCustomerChoices_Id(String id);
}