package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
    Optional<ProductType> findByIdAndIsAvailable(String id, Boolean isAvailable);

}