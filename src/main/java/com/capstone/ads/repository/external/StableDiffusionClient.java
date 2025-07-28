package com.capstone.ads.repository.external;

import com.capstone.ads.config.FeignClientConfig;
import com.capstone.ads.dto.stable_diffusion.TextToImageRequest;
import com.capstone.ads.dto.stable_diffusion.TextToImageResponse;
import com.capstone.ads.dto.stable_diffusion.UpscaleImageRequest;
import com.capstone.ads.dto.stable_diffusion.UpscaleImageResponse;
import com.capstone.ads.dto.stable_diffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressRequest;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "stable-diffusion", url = "${stable-diffusion.url}", configuration = FeignClientConfig.class)
public interface StableDiffusionClient {
    @PostMapping(value = "/sdapi/v1/txt2img", produces = MediaType.APPLICATION_JSON_VALUE)
    TextToImageResponse textToImage(@RequestHeader("Authorization") String vastAiKey,
                                    @RequestBody TextToImageRequest textToImageRequest);

    @PostMapping(value = "/sdapi/v1/extra-single-image", produces = MediaType.APPLICATION_JSON_VALUE)
    UpscaleImageResponse upscaleSingleImage(@RequestHeader("Authorization") String vastAiKey,
                                            @RequestBody UpscaleImageRequest request);

    @PostMapping(value = "/internal/progress", produces = MediaType.APPLICATION_JSON_VALUE)
    ProgressResponse checkProgress(@RequestHeader("Authorization") String vastAiKey,
                                   @RequestBody ProgressRequest progressRequest);

    @GetMapping(value = "/internal/pending-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    PendingTaskResponse checkPendingTask(@RequestHeader("Authorization") String vastAiKey);

}
