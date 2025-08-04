package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.conversation.ConversationDTO;
import com.capstone.ads.service.ConversationService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "CONVERSATION")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationCotroller {
    ConversationService conversationService;

    @PostMapping("/conversation")
    @Operation(summary = "Create a new conversation")
    public ApiResponse<ConversationDTO> createConversation(
            @RequestParam String topicId) {
        ConversationDTO createdConversation = conversationService.createConversation(topicId);
        return ApiResponseBuilder.buildSuccessResponse("Conversation created successfully", createdConversation);
    }

    @GetMapping("/conversation")
    @Operation(summary = "View all conversations")
    public ApiResponse<List<ConversationDTO>> viewAllConversations() {
        List<ConversationDTO> conversations = conversationService.getAllConversations();
        return ApiResponseBuilder.buildSuccessResponse("Conversations retrieved successfully", conversations);
    }

    @GetMapping("/conversation/{id}")
    @Operation(summary = "View conversation details")
    public ApiResponse<ConversationDTO> viewConversationDetails(@PathVariable String id) {
        ConversationDTO conversation = conversationService.getConversationById(id);
        return ApiResponseBuilder.buildSuccessResponse("Conversation details retrieved successfully.", conversation);
    }

    @GetMapping("/topic/{topicId}/conversation")
    @Operation(summary = "View all conversations by topic")
    public ApiPagingResponse<ConversationDTO> viewConversationsByTopicId(@PathVariable String topicId,
                                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var conversations = conversationService.getConversationsByTopicId(topicId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Conversations retrieved successfully", conversations, page);
    }

    @GetMapping("/user/{userId}/conversation")
    @Operation(summary = "View all conversations by user")
    public ApiPagingResponse<ConversationDTO> viewConversationsByUserId(@PathVariable String userId,
                                                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var conversations = conversationService.getConversationsByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Conversations retrieved successfully.", conversations, page);
    }

    @GetMapping("/model/{modelChatBotId}/conversation")
    @Operation(summary = "View all conversations by model chat bot")
    public ApiPagingResponse<ConversationDTO> viewConversationsByModelChatBotId(@PathVariable String modelChatBotId,
                                                                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var conversations = conversationService.getConversationsByModelChatBotId(modelChatBotId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Conversations retrieved successfully.", conversations, page);
    }

    @PutMapping("/conversation/{id}")
    @Operation(summary = "Update a conversation")
    public ApiResponse<ConversationDTO> updateConversation(
            @PathVariable String id,
            @Valid @RequestBody ConversationDTO conversationDTO) {
        ConversationDTO updatedConversation = conversationService.updateConversation(id, conversationDTO);
        return ApiResponseBuilder.buildSuccessResponse("Conversation updated successfully.", updatedConversation);
    }

    @DeleteMapping("/conversation/{id}")
    @Operation(summary = "Delete a conversation")
    public ApiResponse<Void> deleteConversation(@PathVariable String id) {
        conversationService.deleteConversation(id);
        return ApiResponseBuilder.buildSuccessResponse("Conversation deleted successfully.", null);
    }
}
