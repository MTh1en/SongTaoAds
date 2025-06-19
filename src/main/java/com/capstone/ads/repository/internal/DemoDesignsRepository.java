package com.capstone.ads.repository.internal;

import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.enums.DemoDesignStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemoDesignsRepository extends JpaRepository<DemoDesigns, String> {
    Optional<DemoDesigns> findByIdAndStatus(String id, DemoDesignStatus status);

    Page<DemoDesigns> findByCustomDesignRequests_Id(String id, Pageable pageable);

    boolean existsByCustomDesignRequests_IdAndStatusIn(String id, List<DemoDesignStatus> statuses);

    int countByCustomDesignRequests_Id(String id);
}