package com.capstone.ads.repository.internal;

import com.capstone.ads.model.AttributeValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValuesRepository extends JpaRepository<AttributeValues, String> {
    Page<AttributeValues> findByAttributes_Id(String id, Pageable pageable);
}