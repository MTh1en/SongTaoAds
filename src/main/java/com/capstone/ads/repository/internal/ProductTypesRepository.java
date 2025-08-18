package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypesRepository extends JpaRepository<ProductTypes, String> {
    Optional<ProductTypes> findByIdAndIsAvailable(String id, Boolean isAvailable);

    Page<ProductTypes> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    int countByIsAvailable(Boolean isAvailable);

    int countByIsAiGenerated(Boolean isAiGenerated);
}