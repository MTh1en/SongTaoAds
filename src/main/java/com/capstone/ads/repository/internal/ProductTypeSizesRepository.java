package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypeSizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeSizesRepository extends JpaRepository<ProductTypeSizes, String> {
    List<ProductTypeSizes> findByProductTypes_Id(String id);

    boolean existsByProductTypes_IdAndSizes_Id(String productTypeId, String sizeId);
}