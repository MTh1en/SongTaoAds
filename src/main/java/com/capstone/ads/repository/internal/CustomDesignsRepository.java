package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomDesigns;
import com.capstone.ads.model.enums.CustomDesignStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDesignsRepository extends JpaRepository<CustomDesigns, String> {
    Optional<CustomDesigns> findByIdAndStatus(String id, CustomDesignStatus status);

    Page<CustomDesigns> findByCustomDesignRequests_Id(String id, Pageable pageable);

    boolean existsByCustomDesignRequests_IdAndStatusIn(String id, List<CustomDesignStatus> statuses);
}