package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    Optional<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);

    @EntityGraph(attributePaths = {
            "productTypes",
            "productTypes.attributes", // Quan trọng: load cả attributes
            "customerChoiceDetails",
            "customerChoiceSizes"
    })
    Optional<CustomerChoices> findWithDetailsById(String id);
}