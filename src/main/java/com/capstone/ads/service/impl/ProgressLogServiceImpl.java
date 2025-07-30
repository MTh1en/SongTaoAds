package com.capstone.ads.service.impl;

import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;
import com.capstone.ads.event.OrderStatusChangedEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProgressLogMapper;
import com.capstone.ads.model.FileData;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.ProgressLogs;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.ProgressLogsRepository;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.ProgressLogService;
import com.capstone.ads.utils.SecurityContextUtils;
import com.capstone.ads.validator.OrderStateValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProgressLogServiceImpl implements ProgressLogService {
    OrderService orderService;
    FileDataService fileDataService;
    ProgressLogMapper progressLogMapper;
    OrderStateValidator orderStateValidator;
    ProgressLogsRepository progressLogsRepository;
    SecurityContextUtils securityContextUtils;

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderStatusChangedEvent(OrderStatusChangedEvent event) {
        ProgressLogs log = ProgressLogs.builder()
                .orders(orderService.getOrderById(event.getOrderId()))
                .status(event.getOrderStatus())
                .createdBy(event.getUserId())
                .build();
        progressLogsRepository.save(log);
    }

    @Override
    @Transactional
    public ProgressLogDTO createProgressLog(String orderId, ProgressLogCreateRequest request) {
        String userId = securityContextUtils.getCurrentUserId();
        Orders order = orderService.getOrderById(orderId);
        List<OrderStatus> validStatus = Arrays.asList(
                OrderStatus.PRODUCING,
                OrderStatus.PRODUCTION_COMPLETED,
                OrderStatus.DELIVERING,
                OrderStatus.INSTALLED
        );
        if (!validStatus.contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_PROGRESS_LOG_STATUS);
        }
        ProgressLogs log = progressLogMapper.mapCreateRequestToEntity(request);
        log.setOrders(order);
        log.setCreatedBy(userId);
        log = progressLogsRepository.save(log);

        if (request.getProgressLogImages() != null && !request.getProgressLogImages().isEmpty()) {
            fileDataService.uploadMultipleFiles(
                    request.getProgressLogImages(),
                    FileTypeEnum.PROGRESS_LOG,
                    log,
                    FileData::setProgressLogs,
                    (id, size) -> generateProgressLogImagesKey(order.getId(), order.getStatus(), size)
            );
        }

        orderStateValidator.validateTransition(order.getStatus(), request.getStatus());
        orderService.updateOrderStatus(order.getId(), request.getStatus());

        return progressLogMapper.toDTO(log);
    }

    @Override
    public Page<ProgressLogDTO> findProgressLogByOrderId(String orderId, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return progressLogsRepository.findByOrders_Id(orderId, pageable)
                .map(progressLogMapper::toDTO);
    }

    // INTERNAL FUNCTION //
    private List<String> generateProgressLogImagesKey(String orderId, OrderStatus orderStatus, Integer amountKey) {
        List<String> keys = new ArrayList<>();
        IntStream.range(0, amountKey)
                .forEach(i -> keys.add(String.format("orders/%s/%s/%s", orderId, orderStatus, UUID.randomUUID())));
        return keys;
    }
}
