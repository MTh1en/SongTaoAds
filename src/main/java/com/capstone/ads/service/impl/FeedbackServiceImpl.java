package com.capstone.ads.service.impl;

import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.feedback.FeedbackResponseRequest;
import com.capstone.ads.dto.feedback.FeedbackSendRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.FeedbackMapper;
import com.capstone.ads.model.Feedbacks;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.FeedbacksRepository;
import com.capstone.ads.service.FeedbackService;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackServiceImpl implements FeedbackService {
    OrderService orderService;
    FileDataService fileDataService;
    FeedbacksRepository feedbacksRepository;
    FeedbackMapper feedbackMapper;
    SecurityContextUtils securityContextUtils;

    @Override
    @Transactional
    public FeedbackDTO sendFeedback(String orderId, FeedbackSendRequest request) {
        Users currentUser = securityContextUtils.getCurrentUser();
        Orders orders = orderService.getOrderById(orderId);

        Feedbacks feedbacks = feedbackMapper.mapSendRequestToEntity(request);
        feedbacks.setOrders(orders);
        feedbacks.setSendBy(currentUser);
        feedbacks = feedbacksRepository.save(feedbacks);

        return feedbackMapper.toDTO(feedbacks);
    }

    @Override
    @Transactional
    public FeedbackDTO uploadFeedbackImage(String feedbackId, MultipartFile feedbackImage) {
        Feedbacks feedbacks = getFeedbackById(feedbackId);

        String feedbackImageUrl = uploadFeedbackImageToS3(feedbackId, feedbackImage);
        feedbacks.setFeedbackImageUrl(feedbackImageUrl);
        feedbacks = feedbacksRepository.save(feedbacks);

        return feedbackMapper.toDTO(feedbacks);
    }

    @Override
    @Transactional
    public FeedbackDTO responseFeedback(String feedbackId, FeedbackResponseRequest request) {
        Feedbacks feedbacks = getFeedbackById(feedbackId);

        feedbackMapper.mapResponseRequestToEntity(request, feedbacks);
        feedbacks = feedbacksRepository.save(feedbacks);

        return feedbackMapper.toDTO(feedbacks);
    }

    @Override
    public List<FeedbackDTO> findFeedbackByOrderId(String orderId) {
        return feedbacksRepository.findByOrders_Id(orderId).stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FeedbackDTO> findFeedbackByUserId(String userId, int page, int size) {
        Sort sort = Sort.by("sendAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return feedbacksRepository.findBySendBy_Id(userId, pageable)
                .map(feedbackMapper::toDTO);
    }

    @Override
    public Page<FeedbackDTO> findAllFeedback(int page, int size) {
        Sort sort = Sort.by("sendAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return feedbacksRepository.findAll(pageable)
                .map(feedbackMapper::toDTO);
    }

    @Override
    public void hardDeleteFeedback(String feedbackId) {
        if (!feedbacksRepository.existsById(feedbackId)) {
            throw new AppException(ErrorCode.FEEDBACK_NOT_FOUND);
        }
        feedbacksRepository.deleteById(feedbackId);
    }

    public Feedbacks getFeedbackById(String feedbackId) {
        return feedbacksRepository.findById(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
    }

    private String generateFeedbackImageKey(String feedbackId) {
        return String.format("feedback/%s/%s", feedbackId, UUID.randomUUID());
    }

    private String uploadFeedbackImageToS3(String feedbackId, MultipartFile feedbackImage) {
        String feedbackImageKey = generateFeedbackImageKey(feedbackId);
        fileDataService.uploadSingleFile(feedbackImageKey, feedbackImage);
        return feedbackImageKey;
    }
}
