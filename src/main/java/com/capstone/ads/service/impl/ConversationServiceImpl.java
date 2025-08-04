package com.capstone.ads.service.impl;

import com.capstone.ads.dto.conversation.ConversationDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ConversationMapper;
import com.capstone.ads.model.Conversation;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.model.Topic;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.ConversationRepository;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.repository.internal.TopicRepository;
import com.capstone.ads.service.ConversationService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final TopicRepository topicRepository;
    private final ModelChatBotRepository modelChatBotRepository;
    private final ConversationMapper conversationMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    public ConversationDTO createConversation(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
        Users user = securityContextUtils.getCurrentUser();
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        String modelChatBotId=modelChatBot.getId();
        Conversation conversation = conversationMapper.createConversation(topicId, user.getId(), modelChatBotId);
        conversation.setUser(user);
        conversation.setModelChatBot(modelChatBot);
        conversation.setTopic(topic);
        Conversation savedConversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(savedConversation);
        }

    @Override
    public List<ConversationDTO> getAllConversations() {
        return conversationRepository.findAll().stream()
                .map(conversationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDTO getConversationById(String id) {
        return conversationRepository.findById(id)
                .map(conversationMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
    }

    @Override
    public Page<ConversationDTO> getConversationsByUserId(String userId, int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return conversationRepository.findByUser_Id(userId, pageable)
                .map(conversationMapper::toDto);
    }

    @Override
    public Page<ConversationDTO> getConversationsByModelChatBotId(String modelChatBotId, int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return conversationRepository.findByModelChatBot_Id(modelChatBotId, pageable)
                .map(conversationMapper::toDto);
    }

    @Override
    public Page<ConversationDTO> getConversationsByTopicId(String topicId, int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return conversationRepository.findByTopic_Id(topicId, pageable)
                .map(conversationMapper::toDto);
    }

    @Override
    public ConversationDTO updateConversation(String id, ConversationDTO conversationDTO) {
        Conversation existingConversation = conversationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        existingConversation.setPriceQuote(conversationDTO.getPriceQuote());
        Conversation updatedConversation = conversationRepository.save(existingConversation);
        return conversationMapper.toDto(updatedConversation);
    }

    @Override
    public void deleteConversation(String id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        conversationRepository.delete(conversation);
    }
}
