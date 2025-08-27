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
    @Operation(summary = "Tạo topic")
    public ApiResponse<TopicDTO> createTopic(
            @RequestBody TopicCreateRequest topic) {
        TopicDTO createdTopic = topicService.createTopic(topic);
        return ApiResponseBuilder.buildSuccessResponse("Tạo topic thành công", createdTopic);
    }

    @GetMapping("/topics")
    @Operation(summary = "Xem tất cả topic")
    public ApiResponse<List<TopicDTO>> findAllTopics() {
        var topics = topicService.findAllTopics();
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả topic thành công", topics);
    }

    @GetMapping("/topics/{id}")
    @Operation(summary = "Xem topic theo ID")
    public ApiResponse<TopicDTO> viewTopicDetails(@PathVariable String id) {
        TopicDTO topic = topicService.findTopicById(id);
        return ApiResponseBuilder.buildSuccessResponse("Xem topic theo ID thành công", topic);
    }

    @PutMapping("/topics/{id}")
    @Operation(summary = "Cập nhật topic")
    public ApiResponse<TopicDTO> updateTopic(
            @PathVariable String id,
            @Valid @RequestBody TopicUpdateInformationRequest request) {
        TopicDTO updatedTopic = topicService.updateTopic(id, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật topic thành công", updatedTopic);
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "Xóa topic")
    public ApiResponse<Void> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ApiResponseBuilder.buildSuccessResponse("Xóa topic thành công", null);
    }

}
