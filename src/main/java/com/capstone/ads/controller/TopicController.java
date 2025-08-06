package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.topic.TopicCreateRequest;
import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicUpdateInformationRequest;
import com.capstone.ads.service.TopicService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "TOPIC")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {
    TopicService topicService;

    @PostMapping("/topics")
    @Operation(summary = "Create a new topic")
    public ApiResponse<TopicDTO> createTopic(
            @RequestBody TopicCreateRequest topic) {
        TopicDTO createdTopic = topicService.createTopic(topic);
        return ApiResponseBuilder.buildSuccessResponse("Topic created successfully", createdTopic);
    }

    @GetMapping("/topics")
    @Operation(summary = "View all topics")
    public ApiResponse<List<TopicDTO>> viewAllTopics() {
        var topics = topicService.getAllTopics();
        return ApiResponseBuilder.buildSuccessResponse("Topics retrieved successfully", topics);
    }

    @GetMapping("/topics/{id}")
    @Operation(summary = "View topic details")
    public ApiResponse<TopicDTO> viewTopicDetails(@PathVariable String id) {
        TopicDTO topic = topicService.findTopicById(id);
        return ApiResponseBuilder.buildSuccessResponse("Topic details retrieved successfully", topic);
    }

    @PutMapping("/topics/{id}")
    @Operation(summary = "Update a topic")
    public ApiResponse<TopicDTO> updateTopic(
            @PathVariable String id,
            @Valid @RequestBody TopicUpdateInformationRequest request) {
        TopicDTO updatedTopic = topicService.updateTopic(id, request);
        return ApiResponseBuilder.buildSuccessResponse("Topic updated successfully", updatedTopic);
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "Delete a topic")
    public ApiResponse<Void> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ApiResponseBuilder.buildSuccessResponse("Topic deleted successfully", null);
    }

}
