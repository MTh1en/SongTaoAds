package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileInformation;
import com.capstone.ads.dto.stable_diffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StableDiffusionService {
    FileInformation generateImageFromDesignTemplate(String designTemplateId, String prompt, Integer width, Integer height);

    FileInformation resizeImageFromBackground(String backgroundId, Integer width, Integer height);

    ProgressResponse checkProgressByTaskId();

    PendingTaskResponse checkPendingTask();
}
