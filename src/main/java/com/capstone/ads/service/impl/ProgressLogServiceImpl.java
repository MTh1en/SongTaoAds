package com.capstone.ads.service.impl;

import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;
import com.capstone.ads.event.OrderStatusChangedEvent;
import com.capstone.ads.mapper.ProgressLogMapper;
import com.capstone.ads.model.FileData;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.ProgressLogs;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.FileDataRepository;
import com.capstone.ads.repository.internal.ProgressLogsRepository;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.ProgressLogService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProgressLogServiceImpl implements ProgressLogService {
    OrderService orderService;
    FileDataService fileDataService;
    FileDataRepository fileDataRepository;
    ProgressLogMapper progressLogMapper;
    ProgressLogsRepository progressLogsRepository;
    SecurityContextUtils securityContextUtils;

//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void handleOrderStatusChangedEvent(OrderStatusChangedEvent event) {
//        ProgressLogs log = ProgressLogs.builder()
//                .orders(orderService.getOrderById(event.getOrderId()))
//                .status(event.getOrderStatus())
//                .createdBy(event.getUserId())
//                .build();
//        progressLogsRepository.save(log);
//
//        List<FileData> savedFileDate = new ArrayList<>();
//        for (String imageUrl : event.getImageUrls()) {
//            FileData fileData = fileDataService.getFileDataById(imageUrl);
//            fileData.setProgressLogs(log);
//            savedFileDate.add(fileData);
//        }
//        fileDataRepository.saveAll(savedFileDate);
//    }

    public ProgressLogDTO createProgressLog(ProgressLogCreateRequest request) {
        String userId = securityContextUtils.getCurrentUserId();
        Orders order = orderService.getOrderById(request.getOrderId());

        ProgressLogs log = progressLogMapper.mapCreateRequestToEntity(request);
        log.setOrders(order);
        log.setCreatedBy(userId);
        log = progressLogsRepository.save(log);

        fileDataService.uploadMultipleFiles(
                request.getProgressLogImages(),
                FileTypeEnum.LOG,
                log,
                FileData::setProgressLogs,
                (id, size) -> generateProgressLogImagesKey(order.getId(), size)
        );

        return progressLogMapper.toDTO(log);
    }

    private List<String> generateProgressLogImagesKey(String orderId, Integer amountKey) {
        List<String> keys = new ArrayList<>();
        IntStream.range(0, amountKey)
                .forEach(i -> keys.add(String.format("orders/%s/logs/%s", orderId, UUID.randomUUID())));
        return keys;
    }
}
