package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ProgressLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressLogsRepository extends JpaRepository<ProgressLogs, String> {
}