package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProductTypeSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTypeSizeRepository extends JpaRepository<ProductTypeSize, String> {
    List<ProductTypeSize> findByProductType_Id(String id);

}