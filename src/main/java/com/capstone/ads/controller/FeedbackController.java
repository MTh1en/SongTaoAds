package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.feedback.FeedbackResponseRequest;
import com.capstone.ads.dto.feedback.FeedbackSendRequest;
import com.capstone.ads.service.FeedbackService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "FEEDBACK")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {
    FeedbackService feedbackService;

    @PostMapping("/orders/{orderId}/feedbacks")
    @Operation(summary = "Gửi feedback sau khi hoàn tất đơn hàng")
    public ApiResponse<FeedbackDTO> sendFeedback(@PathVariable String orderId,
                                                 @Valid @RequestBody FeedbackSendRequest request) {
        var response = feedbackService.sendFeedback(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Gửi feedback thành công", response);
    }

    @PatchMapping(value = "/feedbacks/{feedbackId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "cập nhật hình ảnh feedback")
    public ApiResponse<FeedbackDTO> uploadFeedbackImage(@PathVariable String feedbackId,
                                                        @RequestPart MultipartFile feedbackImage) {
        var response = feedbackService.uploadFeedbackImage(feedbackId, feedbackImage);
        return ApiResponseBuilder.buildSuccessResponse("Upload hình ảnh feedback thành công", response);
    }

    @PatchMapping("/feedbacks/{feedbackId}/response")
    @Operation(summary = "Phản hồi feedback của khách hàng")
    public ApiResponse<FeedbackDTO> responseFeedback(@PathVariable String feedbackId,
                                                     @Valid @RequestBody FeedbackResponseRequest request) {
        var response = feedbackService.responseFeedback(feedbackId, request);
        return ApiResponseBuilder.buildSuccessResponse("Phản hồi feedback thành công", response);
    }

    @GetMapping("/orders/{orderId}/feedbacks")
    @Operation(summary = "Xem phản hồi theo đơn hàng")
    public ApiResponse<List<FeedbackDTO>> findFeedbackByOrderId(@PathVariable String orderId) {
        var response = feedbackService.findFeedbackByOrderId(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xem phản hồi theo đơn hàng thành công", response);
    }

    @GetMapping("/users/{userId}/feedbacks")
    @Operation(summary = "Xem phản hồi theo người dùng")
    public ApiPagingResponse<FeedbackDTO> findFeedbackByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = feedbackService.findFeedbackByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem phản hồi theo người dùng", response, page);
    }

    @GetMapping("/feedbacks")
    @Operation(summary = "Xem tất cả cá phản hồi")
    public ApiPagingResponse<FeedbackDTO> findAllFeedback(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = feedbackService.findAllFeedback(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả phản hồi", response, page);
    }

    @DeleteMapping("/feedbacks/{feedbackId}")
    @Operation(summary = "Xóa cứng phản hồi")
    public ApiResponse<Void> hardDeleteFeedback(@PathVariable String feedbackId) {
        feedbackService.hardDeleteFeedback(feedbackId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa phản hồi thành công", null);
    }
}
