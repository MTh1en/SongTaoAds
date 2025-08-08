package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDesignRequestsRepository extends JpaRepository<CustomDesignRequests, String> {
    Optional<CustomDesignRequests> findByIdAndStatus(String id, CustomDesignRequestStatus status);

    Optional<CustomDesignRequests> findByIdAndStatusIn(String id, List<CustomDesignRequestStatus> statuses);

    boolean existsByIdAndStatusIn(String id, List<CustomDesignRequestStatus> statuses);

    Page<CustomDesignRequests> findByCustomerDetail_Id(String id, Pageable pageable);

    Page<CustomDesignRequests> findByAssignDesigner_Id(String id, Pageable pageable);

    Page<CustomDesignRequests> findByStatus(CustomDesignRequestStatus status, Pageable pageable);

    Page<CustomDesignRequests> findByIsNeedSupport(Boolean isNeedSupport, Pageable pageable);
}