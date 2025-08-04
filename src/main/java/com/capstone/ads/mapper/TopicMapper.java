package com.capstone.ads.mapper;

import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicRequest;
import com.capstone.ads.model.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDTO toDto(Topic topic);

    Topic toEntity(TopicDTO topicDTO);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "maxQuestion", source = "topicRequest.maxQuestion")
    Topic createTopic(TopicRequest topicRequest);
}