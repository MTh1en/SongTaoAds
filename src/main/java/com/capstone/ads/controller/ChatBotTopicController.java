package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chat_bot_topic.ChatBotTopicResponse;
import com.capstone.ads.service.ChatBotTopicService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CHAT BOT TOPIC")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotTopicController {
    ChatBotTopicService chatBotTopicService;

    @PostMapping("/model-chat/{modelChatBotId}/topics/{topicId}/chat-bot-topic")
    @Operation(summary = "Thêm topic vào model chat bot")
    public ApiResponse<ChatBotTopicResponse> createQuestion(
            @PathVariable String topicId,
            @PathVariable String modelChatBotId) {
        ChatBotTopicResponse response = chatBotTopicService.createChatBotTopic(topicId, modelChatBotId);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot topic tạo thành công", response);
    }

    @GetMapping("/chat-bot-topic")
    @Operation(summary = "Xem tất cả chat bot topic")
    public ApiResponse<List<ChatBotTopicResponse>> getAll() {
        List<ChatBotTopicResponse> response = chatBotTopicService.getAllChatBotTopic();
        return ApiResponseBuilder.buildSuccessResponse("Lấy được tất cả chat bot topic thành công", response);
    }

    @GetMapping("/chat-bot-topic/{id}")
    @Operation(summary = "Xem chat bot topic bằng chat-bot-topic-id")
    public ApiResponse<ChatBotTopicResponse> getById(@PathVariable String id) {
        ChatBotTopicResponse response = chatBotTopicService.getChatBotTopicById(id);
        return ApiResponseBuilder.buildSuccessResponse("Lấy được chat bot topic thành công", response);
    }

    @GetMapping("/model-chat/{modelChatBotId}/chat-bot-topic")
    @Operation(summary = "Xem chat bot topic bằng model chat id")
    public ApiResponse<List<ChatBotTopicResponse>> getByModelChatBot(@PathVariable String modelChatBotId) {
        List<ChatBotTopicResponse> response = chatBotTopicService.getByModelChat(modelChatBotId);
        return ApiResponseBuilder.buildSuccessResponse("Lấy được chat bot topic thành công", response);
    }

    @GetMapping("/topic/{topicId}/chat-bot-topic")
    @Operation(summary = "Xem chat bot topic bằng topic id")
    public ApiResponse<List<ChatBotTopicResponse>> getByTopic(@PathVariable String topicId) {
        List<ChatBotTopicResponse> response = chatBotTopicService.getByTopicId(topicId);
        return ApiResponseBuilder.buildSuccessResponse("Lấy được chat bot topic thành công", response);
    }

    @DeleteMapping("/chat-bot-topic/{id}")
    @Operation(summary = "Xóa chat bot topic")
    public ApiResponse<Void> delete(@PathVariable String id) {
        chatBotTopicService.deleteChatBotTopic(id);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot topic đã xóa thành công", null);
    }

    @PostMapping("/model-chat/{modelChatBotId}/chat-bot-topic")
    @Operation(summary = "Thêm topic từ model trước đó")
    public ApiResponse<List<ChatBotTopicResponse>> addOldTopic(
            @PathVariable String modelChatBotId) {
        List<ChatBotTopicResponse> response = chatBotTopicService.addTopicsFromOldModel(modelChatBotId);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot topic được thêm thành công", response);
    }
}
