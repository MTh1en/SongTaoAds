package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomDesigns;
import com.capstone.ads.model.enums.CustomDesignStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomDesignsRepository extends JpaRepository<CustomDesigns, String> {
    Optional<CustomDesigns> findByIdAndStatus(String id, CustomDesignStatus status);

    List<CustomDesigns> findByCustomDesignRequests_Id(String id);

    boolean existsByCustomDesignRequests_IdAndStatusIn(String id, List<CustomDesignStatus> statuses);

}