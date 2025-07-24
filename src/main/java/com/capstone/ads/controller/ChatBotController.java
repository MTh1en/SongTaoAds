package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @PostMapping("/test-chat")
    @Operation(summary = "Staff Test Model Chat")
    public ApiResponse<String> chat(@RequestBody TestChatRequest request) {
        String reply = chatBotService.TestChat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @PostMapping("/translate-to-txt2img-prompt")
    public ApiResponse<String> translate(@RequestBody ChatRequest request) {
        String reply = chatBotService.translateToTextToImagePrompt(request.getPrompt());
        return ApiResponseBuilder.buildSuccessResponse("Translate retrieved successfully.", reply);
    }

    @GetMapping("/models")
    @Operation(summary = "Xem tất cả các model open-ai")
    public ApiResponse<ListModelsResponse> getModels() {
        ListModelsResponse response = chatBotService.getModels();
        return ApiResponseBuilder.buildSuccessResponse("Models retrieved successfully", response);
    }

    @PostMapping("/pricing/traditional")
    @Operation(summary = "Báo giá bảng quảng cáo truyền thống bằng chatbot")
    public ApiResponse<ResponsePricingChat> getTraditionalBillboardPricing(@RequestBody TraditionalBillboardRequest request) {
        ResponsePricingChat reply = chatBotService.getTraditionalBillboardPricing(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @PostMapping("/pricing/modern")
    @Operation(summary = "Báo giá bảng quảng cáo hiện đại bằng chatbot")
    public ApiResponse<ResponsePricingChat> getModernBillboardPricing(@RequestBody ModernBillboardRequest request) {
        ResponsePricingChat reply = chatBotService.getModernBillboardPricing(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @GetMapping("/frequent-questions")
    @Operation(summary = "Xem top 10 câu hỏi được hỏi nhiều nhất")
    public ApiResponse<List<FrequentQuestion>> getTop10FrequentQuestions() {
        List<FrequentQuestion> response = chatBotService.getTop10FrequentQuestions();
        return ApiResponseBuilder.buildSuccessResponse("Questions retrieved successfully", response);
    }

}
