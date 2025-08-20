package com.capstone.ads.service;

import com.capstone.ads.dto.chat_bot_topic.ChatBotTopicResponse;

import java.util.List;

public interface ChatBotTopicService {
    ChatBotTopicResponse createChatBotTopic(String topId, String modelChatBotId);

    List<ChatBotTopicResponse> getAllChatBotTopic();

    ChatBotTopicResponse getChatBotTopicById(String id);

    void deleteChatBotTopic(String id);

    List<ChatBotTopicResponse> getByModelChat(String modelChatBotId);

    List<ChatBotTopicResponse> getByTopicId(String topicId);

    List<ChatBotTopicResponse> addTopicsFromOldModel(String modelChatBotId);
}
