package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypeSizes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTypeSizesRepository extends JpaRepository<ProductTypeSizes, String> {
    List<ProductTypeSizes> findByProductTypes_Id(String id);

    boolean existsByProductTypes_IdAndSizes_Id(String productTypeId, String sizeId);
}