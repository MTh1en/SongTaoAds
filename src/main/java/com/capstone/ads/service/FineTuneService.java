package com.capstone.ads.service;

import com.capstone.ads.dto.fine_tune.FileDeletionResponse;
import com.capstone.ads.dto.fine_tune.FileUploadResponse;
import com.capstone.ads.dto.fine_tune.FineTuningJobRequest;
import com.capstone.ads.dto.fine_tune.FineTuningJobResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FineTuneService {
    FileUploadResponse uploadFileToFineTune(MultipartFile file);

    FineTuningJobResponse fineTuningJob(FineTuningJobRequest request);

    FileUploadResponse getUploadedFileById(String fileId);

    byte[] getContentFileById(String fileId);

    List<FileUploadResponse> getAllUploadedFiles();

    FileDeletionResponse deleteUploadedFile(String fileId);

    List<FineTuningJobResponse> getAllFineTuneJobs();

    FineTuningJobResponse getFineTuningJob(String jobId);

    FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId);
}
