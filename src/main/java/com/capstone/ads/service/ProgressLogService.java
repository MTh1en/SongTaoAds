package com.capstone.ads.service;

import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;

public interface ProgressLogService {
    ProgressLogDTO createProgressLog(ProgressLogCreateRequest request);
}
