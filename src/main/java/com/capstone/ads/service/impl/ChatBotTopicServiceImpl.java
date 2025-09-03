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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        String prevName = newModel.getPreviousModelName();

        ModelChatBot oldModel = modelChatService.getModelChatBotByName(prevName);

        List<ChatBotTopic> oldLinks = chatBotTopicRepository.findByModelChatBot_Id(oldModel.getId());

        Set<String> existedTopicIds = chatBotTopicRepository.findByModelChatBot_Id(newModel.getId())
                .stream()
                .map(link -> link.getTopic().getId())
                .collect(Collectors.toSet());

        List<ChatBotTopic> toSave = oldLinks.stream()
                .map(ChatBotTopic::getTopic)
                .filter(t -> !existedTopicIds.contains(t.getId()))
                .map(t -> ChatBotTopic.builder()
                        .modelChatBot(newModel)
                        .topic(t)
                        .build())
                .collect(Collectors.toList());

        if (toSave.isEmpty()) return Collections.emptyList();

        return chatBotTopicRepository.saveAll(toSave)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ChatBotTopic findChatBotTopicById(String chatBotTopicId){
        return chatBotTopicRepository.findById(chatBotTopicId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_BOT_TOPIC_NOT_FOUND));
    }

}
