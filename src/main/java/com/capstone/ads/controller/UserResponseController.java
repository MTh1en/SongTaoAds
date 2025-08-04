package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.conversation.ConversationDTO;
import com.capstone.ads.dto.user_response.UserResponseDTO;
import com.capstone.ads.dto.user_response.UserResponseRequest;
import com.capstone.ads.model.Conversation;
import com.capstone.ads.service.ConversationService;
import com.capstone.ads.service.UserResponseService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-responses")
@RequiredArgsConstructor
@Tag(name = "USER RESPONSE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResponseController {

    UserResponseService userResponseService;
    ConversationService conversationService;

    @PostMapping
    @Operation(summary = "Create a new user response")
    public ApiResponse<UserResponseDTO> createUserResponse(
            @RequestBody UserResponseRequest userResponseRequest,
            @RequestParam String questionId,
            @RequestParam String conversationId) {
        UserResponseDTO createdUserResponse = userResponseService.createUserResponse(questionId, conversationId, userResponseRequest);
        return ApiResponseBuilder.buildSuccessResponse("User response created .", createdUserResponse);
    }

    @GetMapping
    @Operation(summary = "View all user responses")
    public ApiResponse<List<UserResponseDTO>> viewAllUserResponses() {
        List<UserResponseDTO> userResponses = userResponseService.getAllUserResponses();
        return ApiResponseBuilder.buildSuccessResponse("User responses retrieved .", userResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "View user response details")
    public ApiResponse<UserResponseDTO> viewUserResponseDetails(@PathVariable String id) {
        UserResponseDTO userResponse = userResponseService.getUserResponseById(id);
        return ApiResponseBuilder.buildSuccessResponse("User response details retrieved .", userResponse);
    }

    @GetMapping("/question/{questionId}")
    @Operation(summary = "View all user responses by question")
    public ApiResponse<List<UserResponseDTO>> viewUserResponsesByQuestionId(@PathVariable String questionId) {
        List<UserResponseDTO> userResponses = userResponseService.getUserResponsesByQuestionId(questionId);
        return ApiResponseBuilder.buildSuccessResponse("User responses retrieved .", userResponses);
    }

    @GetMapping("/conversation/{conversationId}")
    @Operation(summary = "View all user responses by conversation")
    public ApiResponse<List<UserResponseDTO>> viewUserResponsesByConversationId(@PathVariable String conversationId) {
        List<UserResponseDTO> userResponses = userResponseService.getUserResponsesByConversationId(conversationId);
        return ApiResponseBuilder.buildSuccessResponse("User responses retrieved .", userResponses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user response")
    public ApiResponse<UserResponseDTO> updateUserResponse(
            @PathVariable String id,
            @Valid @RequestBody UserResponseDTO userResponseDTO) {
        UserResponseDTO updatedUserResponse = userResponseService.updateUserResponse(id, userResponseDTO);
        return ApiResponseBuilder.buildSuccessResponse("User response updated .", updatedUserResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user response")
    public ApiResponse<Void> deleteUserResponse(@PathVariable String id) {
        userResponseService.deleteUserResponse(id);
        return ApiResponseBuilder.buildSuccessResponse("User response deleted .", null);
    }

    @PostMapping("/conversations/{conversationId}/generate-pricing-quote")
    @Operation(summary = "Generate a pricing quote for the conversation")
    public ApiResponse<List<String>> generatePricingQuote(
            @PathVariable String conversationId) {
       List<String> response =  userResponseService.generatePricingQuotePrompt(conversationId);
        return ApiResponseBuilder.buildSuccessResponse("Pricing quote success .", response);
    }
}
