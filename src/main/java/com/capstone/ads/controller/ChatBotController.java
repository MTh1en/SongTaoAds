package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat-bot")
@RequiredArgsConstructor
@Tag(name = "CHAT BOT")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotController {
    ChatBotService chatBotService;

    @PostMapping("/chat")
    @Operation(summary = "Chat với chatbot")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.chat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot phản hồi thành công", reply);
    }

    @PostMapping("/test-chat")
    @Operation(summary = "Staff Test Model Chat")
    public ApiResponse<String> chat(@RequestBody TestChatRequest request) {
        String reply = chatBotService.TestChat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot phản hồi thành công", reply);
    }

    @PostMapping("/translate-to-txt2img-prompt")
    public ApiResponse<String> translate(@RequestBody ChatRequest request) {
        String reply = chatBotService.translateToTextToImagePrompt(request.getPrompt());
        return ApiResponseBuilder.buildSuccessResponse("Chuyển promt hình ảnh thành công", reply);
    }

    @GetMapping("/models")
    @Operation(summary = "Xem tất cả các model open-ai")
    public ApiResponse<ListModelsResponse> getModels() {
        ListModelsResponse response = chatBotService.getModels();
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả các model trên open-ai", response);
    }

    @PostMapping("/pricing/traditional")
    @Operation(summary = "Báo giá bảng quảng cáo truyền thống bằng chatbot")
    public ApiResponse<TraditionalBillboardResponse> getTraditionalBillboardPricing(@RequestBody TraditionalBillboardRequest request) {
        var response = chatBotService.getTraditionalBillboardPricing(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot phản hồi thành công", response);
    }

    @PostMapping("/pricing/modern")
    @Operation(summary = "Báo giá bảng quảng cáo hiện đại bằng chatbot")
    public ApiResponse<ModernBillboardResponse> getModernBillboardPricing(@RequestBody ModernBillboardRequest request) {
        var reply = chatBotService.getModernBillboardPricing(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat bot phản hồi thành công", reply);
    }
}
