package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.dto.stablediffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stablediffusion.progress.ProgressResponse;

public interface StableDiffusionService {
    FileData generateImage(String designTemplateId, String prompt);

    ProgressResponse checkProgressByTaskId();

    PendingTaskResponse checkPendingTask();
}
