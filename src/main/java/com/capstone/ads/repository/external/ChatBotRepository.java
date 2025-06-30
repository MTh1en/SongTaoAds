package com.capstone.ads.repository.external;

import com.capstone.ads.dto.chatBot.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "openai-fine-tuning", url = "https://api.openai.com/v1")
public interface ChatBotRepository {
    @PostMapping(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE)
    ChatCompletionResponse getChatCompletions(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatCompletionRequest requestBody);

    @PostMapping(value = "/fine_tuning/jobs", consumes = MediaType.APPLICATION_JSON_VALUE)
    FineTuningJobResponse createFineTuningJob(
            @RequestHeader("Authorization") String authorization,
            @RequestBody FineTuningJobRequest requestBody);

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileUploadResponse uploadFile(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("purpose") String purpose,
            @RequestPart("file") MultipartFile file);

    @GetMapping("/files")
    FileUploadedListResponse getFiles(
            @RequestHeader("Authorization") String authorization);

    @GetMapping("/files/{fileId}")
    FileUploadResponse getFileById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("fileId") String fileId);

    @DeleteMapping("/files/{fileId}")
    FileDeletionResponse deleteFileById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("fileId") String fileId);


    @GetMapping("/fine_tuning/jobs")
    FineTuningJobListResponse getFineTuningJobs(
            @RequestHeader("Authorization") String authorization);

    @GetMapping("/fine_tuning/jobs/{fine_tuning_job_id}")
    FineTuningJobResponse getFineTuningJobById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("fine_tuning_job_id") String fineTuningJobId);

    @PostMapping(value = "/fine_tuning/jobs/{fine_tuning_job_id}/cancel")
    FineTuningJobResponse cancelFineTuningJob(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("fine_tuning_job_id") String fineTuningJobId);

}


