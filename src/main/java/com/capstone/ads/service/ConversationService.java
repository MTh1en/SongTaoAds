package com.capstone.ads.service;

import com.capstone.ads.dto.conversation.ConversationDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConversationService {

    ConversationDTO createConversation(String topicId);

    List<ConversationDTO> getAllConversations();

    ConversationDTO getConversationById(String id);

    Page<ConversationDTO> getConversationsByUserId(String userId, int page, int size);

    Page<ConversationDTO> getConversationsByModelChatBotId(String modelChatBotId, int page, int size);

    Page<ConversationDTO> getConversationsByTopicId(String topicId, int page, int size);

    ConversationDTO updateConversation(String id, ConversationDTO conversationDTO);

    void deleteConversation(String id);
}
