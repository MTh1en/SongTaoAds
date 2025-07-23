package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProgressLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressLogsRepository extends JpaRepository<ProgressLogs, String> {
    Page<ProgressLogs> findByOrders_Id(String id, Pageable pageable);
}