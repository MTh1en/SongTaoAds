package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Attributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributesRepository extends JpaRepository<Attributes, String> {
    List<Attributes> findByProductTypes_Id(String id);

    List<Attributes> findByProductTypes_IdAndIsAvailable(String id, Boolean isAvailable);

    Optional<Attributes> findByIdAndIsAvailable(String id, Boolean isAvailable);
}