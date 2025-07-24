package com.capstone.ads.service;

import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;
import org.springframework.data.domain.Page;

public interface ProgressLogService {
    ProgressLogDTO createProgressLog(String orderId, ProgressLogCreateRequest request);

    Page<ProgressLogDTO> findProgressLogByOrderId(String orderId, int page, int size);
}
