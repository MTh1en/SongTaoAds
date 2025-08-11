package com.capstone.ads.service;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.fine_tune.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FineTuneService {
    FileUploadResponse uploadFileToFineTune(MultipartFile file);

    FineTuningJobResponse fineTuningJob(CreateFineTuneJobRequest request);

    FileUploadResponse getUploadedFileById(String fileId);

    byte[] getContentFileById(String fileId);

    Page<FileUploadResponse> getFilesPage(int page, int size);

    FileDeletionResponse deleteUploadedFile(String fileId);

    List<FineTuningJobResponse> getAllFineTuneJobs();

    FineTuningJobResponse getFineTuningJob(String jobId);

    FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId);
}
