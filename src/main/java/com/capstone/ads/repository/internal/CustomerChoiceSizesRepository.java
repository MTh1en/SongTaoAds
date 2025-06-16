package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceSizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerChoiceSizesRepository extends JpaRepository<CustomerChoiceSizes, String> {
    boolean existsByCustomerChoices_IdAndSizes_Id(String customerChoiceId, String sizeId);

    List<CustomerChoiceSizes> findByCustomerChoices_Id(String id);
}