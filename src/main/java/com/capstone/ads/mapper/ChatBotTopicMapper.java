package com.capstone.ads.mapper;

import com.capstone.ads.dto.ChatBotTopic.ChatBotTopicCreateRequest;
import com.capstone.ads.dto.ChatBotTopic.ChatBotTopicResponse;
import com.capstone.ads.model.ChatBotTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatBotTopicMapper {
    @Mapping(target ="topicId", source = "topic.id")
    @Mapping(target = "modelChatBotId", source = "modelChatBot.id")
    ChatBotTopicResponse toResponse(ChatBotTopic entity);

    ChatBotTopic mapToCreateRequestToEntity (String topId, String modelChatBotId);
}
