package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.FileDeletionResponse;
import com.capstone.ads.dto.chatBot.FileUploadResponse;
import com.capstone.ads.dto.chatBot.FineTuningJobRequest;
import com.capstone.ads.dto.chatBot.FineTuningJobResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FineTuneService {
    FileUploadResponse uploadFileToFineTune(MultipartFile file);

    FineTuningJobResponse fineTuningJob(FineTuningJobRequest request);

    FileUploadResponse getUploadedFileById(String fileId);

    List<FileUploadResponse> getAllUploadedFiles();

    FileDeletionResponse deleteUploadedFile(String fileId);

    List<FineTuningJobResponse> getAllFineTuneJobs();

    FineTuningJobResponse getFineTuningJob(String jobId);

    FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId);
}
