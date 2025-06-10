package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.stablediffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stablediffusion.progress.ProgressResponse;
import com.capstone.ads.service.StableDiffusionService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class StableDiffusionController {
    private final StableDiffusionService stableDiffusionService;

    @PostMapping(value = "/design-templates/{designTemplateId}/txt2img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generateImageFromDesignTemplate(@PathVariable String designTemplateId,
                                                             @RequestPart String prompt) {
        var response = stableDiffusionService.generateImage(designTemplateId, prompt);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, response.getContentType())
                .body(response.getContent());
    }

    @PostMapping(value = "/stable-diffusion/progress")
    public ApiResponse<ProgressResponse> checkProgress() {
        var response = stableDiffusionService.checkProgressByTaskId();
        return ApiResponseBuilder.buildSuccessResponse("check your progress", response);
    }

    @GetMapping(value = "/stable-diffusion/pending-task")
    public ApiResponse<PendingTaskResponse> checkPendingTask() {
        var response = stableDiffusionService.checkPendingTask();
        return ApiResponseBuilder.buildSuccessResponse("check pending task", response);
    }
}
