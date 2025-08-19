package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDesignRequestsRepository extends JpaRepository<CustomDesignRequests, String> {
    Optional<CustomDesignRequests> findByIdAndStatusIn(String id, List<CustomDesignRequestStatus> statuses);

    Page<CustomDesignRequests> findByCustomerDetail_Id(String id, Pageable pageable);

    Page<CustomDesignRequests> findByAssignDesigner_Id(String id, Pageable pageable);

    Page<CustomDesignRequests> findByStatus(CustomDesignRequestStatus status, Pageable pageable);

    Page<CustomDesignRequests> findByIsNeedSupport(Boolean isNeedSupport, Pageable pageable);

    @EntityGraph(attributePaths = {
            "customerDetail.users",
    })
    @NonNull
    Optional<CustomDesignRequests> findById(@NonNull String id);

    int countByStatusIn(List<CustomDesignRequestStatus> statuses);

    int countByStatusNotIn(List<CustomDesignRequestStatus> statuses);

    List<CustomDesignRequests> findByAssignDesigner(Users assignDesigner);

    int countByUpdatedAtBetween(LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    int countByStatusAndUpdatedAtBetween(CustomDesignRequestStatus status, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    Page<CustomDesignRequests> findByCodeContainsIgnoreCaseOrCustomerDetail_CompanyNameContainsIgnoreCase(String code, String companyName, Pageable pageable);

    Page<CustomDesignRequests> findByCodeContainsIgnoreCaseAndAssignDesigner(String code, Users assignDesigner, Pageable pageable);
}