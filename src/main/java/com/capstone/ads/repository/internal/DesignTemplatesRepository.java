package com.capstone.ads.repository.internal;

import com.capstone.ads.model.DesignTemplates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignTemplatesRepository extends JpaRepository<DesignTemplates, String> {
    Optional<DesignTemplates> findByIdAndIsAvailable(String id, Boolean isAvailable);

    Page<DesignTemplates> findByProductTypes_IdAndIsAvailable(String id, Boolean isAvailable, Pageable pageable);

    Page<DesignTemplates> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    boolean existsByIdAndIsAvailable(String id, Boolean isAvailable);


}