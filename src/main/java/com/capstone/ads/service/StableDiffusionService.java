package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.dto.stable_diffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressResponse;

public interface StableDiffusionService {
    FileData generateImage(String designTemplateId, String prompt);

    ProgressResponse checkProgressByTaskId();

    PendingTaskResponse checkPendingTask();
}
