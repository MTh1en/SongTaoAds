package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomDesignRequestsRepository extends JpaRepository<CustomDesignRequests, String> {
    Optional<CustomDesignRequests> findByIdAndStatus(String id, CustomDesignRequestStatus status);

    Optional<CustomDesignRequests> findByIdAndStatusIn(String id, List<CustomDesignRequestStatus> statuses);

    List<CustomDesignRequests> findByCustomerDetail_Id(String id);

    List<CustomDesignRequests> findByAssignDesigner_Id(String id);

    List<CustomDesignRequests> findByStatus(CustomDesignRequestStatus status);
}