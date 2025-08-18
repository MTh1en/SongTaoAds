package com.capstone.ads.repository.internal;

import com.capstone.ads.model.AttributeValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValuesRepository extends JpaRepository<AttributeValues, String> {
    List<AttributeValues> findByAttributes_Id(String id);

    List<AttributeValues> findByAttributes_IdAndIsAvailable(String id, Boolean isAvailable);

    int countByIsAvailable(Boolean isAvailable);
}