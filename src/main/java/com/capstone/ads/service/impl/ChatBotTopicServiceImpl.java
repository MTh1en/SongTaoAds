package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chat_bot_topic.ChatBotTopicResponse;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ChatBotTopicMapper;
import com.capstone.ads.model.ChatBotTopic;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.internal.ChatBotTopicRepository;
import com.capstone.ads.service.ChatBotTopicService;
import com.capstone.ads.service.ModelChatService;
import com.capstone.ads.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotTopicServiceImpl implements ChatBotTopicService {
    private final ChatBotTopicRepository chatBotTopicRepository;
    private final ModelChatService modelChatService;
    private final TopicService topicService;
    private final ChatBotTopicMapper mapper;

    @Override
    @Transactional
    public ChatBotTopicResponse createChatBotTopic(String topId, String modelChatBotId) {
        ChatBotTopic chatBotTopic = mapper.mapToCreateRequestToEntity(topId, modelChatBotId);
        chatBotTopic.setModelChatBot(modelChatService.getModelChatBotById(modelChatBotId));
        chatBotTopic.setTopic(topicService.getTopicById(topId));

        ChatBotTopic saved = chatBotTopicRepository.save(chatBotTopic);
        return mapper.toResponse(saved);
    }

    @Override
    public List<ChatBotTopicResponse> getAllChatBotTopic() {
        return chatBotTopicRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ChatBotTopicResponse getChatBotTopicById(String chatBotTopicId) {
        ChatBotTopic entity = findChatBotTopicById(chatBotTopicId);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void deleteChatBotTopic(String chatBotTopicId) {
        ChatBotTopic entity = findChatBotTopicById(chatBotTopicId);
        chatBotTopicRepository.delete(entity);
    }

    @Override
    public List<ChatBotTopicResponse> getByModelChat(String modelChatBotId) {
        return chatBotTopicRepository.findByModelChatBot_Id(modelChatBotId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ChatBotTopicResponse> getByTopicId(String topicId) {
        return chatBotTopicRepository.findByTopic_Id(topicId)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    @Override
    @Transactional
    public List<ChatBotTopicResponse> addTopicsFromOldModel(String modelChatBotId) {

        ModelChatBot newModel = modelChatService.getModelChatBotById(modelChatBotId);
        String oldModelId = modelChatService.getModelChatBotByName(newModel.getPreviousModelName()).getId();

        List<ChatBotTopic> oldTopics = chatBotTopicRepository.findByModelChatBot_Id(oldModelId);

        List<ChatBotTopic> newTopics = oldTopics.stream()
                .map(old -> ChatBotTopic.builder()
                        .modelChatBot(newModel)
                        .topic(old.getTopic())
                        .build())
                .toList();

        List<ChatBotTopic> savedTopics = chatBotTopicRepository.saveAll(newTopics);

        return savedTopics.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ChatBotTopic findChatBotTopicById(String chatBotTopicId){
        return chatBotTopicRepository.findById(chatBotTopicId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_BOT_TOPIC_NOT_FOUND));
    }

}
