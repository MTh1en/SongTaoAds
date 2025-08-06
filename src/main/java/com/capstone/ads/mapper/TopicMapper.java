package com.capstone.ads.mapper;

import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicCreateRequest;
import com.capstone.ads.dto.topic.TopicUpdateInformationRequest;
import com.capstone.ads.model.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDTO toDto(Topic topic);

    Topic mapCreateRequestToEntity(TopicCreateRequest topicCreateRequest);

    void mapUpdateRequestToEntity(TopicUpdateInformationRequest request, @MappingTarget Topic topic);
}