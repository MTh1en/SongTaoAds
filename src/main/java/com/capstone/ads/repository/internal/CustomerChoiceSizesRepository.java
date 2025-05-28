package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceSizes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerChoiceSizesRepository extends JpaRepository<CustomerChoiceSizes, String> {
    boolean existsByCustomerChoices_IdAndSizes_Id(String customerChoiceId, String sizeId);

    List<CustomerChoiceSizes> findByCustomerChoices_Id(String id);
}