package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Attributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributesRepository extends JpaRepository<Attributes, String> {
    Page<Attributes> findByProductTypes_Id(String id, Pageable pageable);

    boolean existsByIdAndIsAvailable(String id, Boolean isAvailable);
}