package com.capstone.ads.mapper;

import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;
import com.capstone.ads.model.ProgressLogs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgressLogMapper {
    ProgressLogDTO toDTO(ProgressLogs progressLogs);

    ProgressLogs mapCreateRequestToEntity(ProgressLogCreateRequest request);
}
