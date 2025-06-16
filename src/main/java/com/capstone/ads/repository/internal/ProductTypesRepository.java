package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypesRepository extends JpaRepository<ProductTypes, String> {
    Optional<ProductTypes> findByIdAndIsAvailable(String id, Boolean isAvailable);

    boolean existsByIdAndIsAvailable(String id, Boolean isAvailable);
}