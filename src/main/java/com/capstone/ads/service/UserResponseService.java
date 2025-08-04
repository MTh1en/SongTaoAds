package com.capstone.ads.service;

import com.capstone.ads.dto.user_response.UserResponseDTO;
import com.capstone.ads.dto.user_response.UserResponseRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserResponseService {
    UserResponseDTO createUserResponse(String questionId, String conversationId, UserResponseRequest userResponseRequest);

    List<UserResponseDTO> getAllUserResponses();

    UserResponseDTO getUserResponseById(String id);

    List<UserResponseDTO> getUserResponsesByQuestionId(String questionId);

    List<UserResponseDTO> getUserResponsesByConversationId(String conversationId);

    UserResponseDTO updateUserResponse(String id, UserResponseDTO userResponseDTO);

    void deleteUserResponse(String id);

    @Transactional
    List<String> generatePricingQuotePrompt(String conversationId);
}
