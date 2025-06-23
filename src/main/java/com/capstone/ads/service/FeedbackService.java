package com.capstone.ads.service;

import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.feedback.FeedbackResponseRequest;
import com.capstone.ads.dto.feedback.FeedbackSendRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO sendFeedback(String orderId, FeedbackSendRequest request);

    FeedbackDTO uploadFeedbackImage(String feedbackId, MultipartFile feedbackImage);

    FeedbackDTO responseFeedback(String feedbackId, FeedbackResponseRequest request);

    List<FeedbackDTO> findFeedbackByOrderId(String orderId);

    Page<FeedbackDTO> findFeedbackByUserId(String userId, int page, int size);

    Page<FeedbackDTO> findAllFeedback(int page, int size);

    void hardDeleteFeedback(String feedbackId);
}
