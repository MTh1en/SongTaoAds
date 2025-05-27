package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductTypes, String> {
    Optional<ProductTypes> findByIdAndIsAvailable(String id, Boolean isAvailable);
}