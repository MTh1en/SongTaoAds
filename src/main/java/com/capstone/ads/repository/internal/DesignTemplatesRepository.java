package com.capstone.ads.repository.internal;

import com.capstone.ads.model.DesignTemplates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignTemplatesRepository extends JpaRepository<DesignTemplates, String> {
    Optional<DesignTemplates> findByIdAndIsAvailable(String id, Boolean isAvailable);

    List<DesignTemplates> findByProductTypes_IdAndIsAvailable(String id, Boolean isAvailable);
}