package com.capstone.ads.repository.internal;

import com.capstone.ads.model.AttributeValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValuesRepository extends JpaRepository<AttributeValues, String> {
    List<AttributeValues> findByAttributes_Id(String id);

}