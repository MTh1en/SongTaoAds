package com.capstone.ads.mapper;

import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.feedback.FeedbackResponseRequest;
import com.capstone.ads.dto.feedback.FeedbackSendRequest;
import com.capstone.ads.model.Feedbacks;
import com.capstone.ads.model.enums.FeedbackStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(target = "orderId", source = "orders.id")
    FeedbackDTO toDTO(Feedbacks feedbacks);

    @Mapping(target = "sendAt", expression = "java(initTime())")
    @Mapping(target = "status", expression = "java(initSendStatus())")
    Feedbacks mapSendRequestToEntity(FeedbackSendRequest request);

    @Mapping(target = "responseAt", expression = "java(initTime())")
    @Mapping(target = "status", expression = "java(initResponseStatus())")
    void mapResponseRequestToEntity(FeedbackResponseRequest request, @MappingTarget Feedbacks feedbacks);

    default FeedbackStatus initSendStatus() {
        return FeedbackStatus.PENDING;
    }

    default FeedbackStatus initResponseStatus() {
        return FeedbackStatus.ANSWERED;
    }

    default LocalDateTime initTime() {
        return LocalDateTime.now();
    }
}
