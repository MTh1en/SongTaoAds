package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.FrequentQuestion;
import com.capstone.ads.service.ChatBotLogService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat-bot")
@RequiredArgsConstructor
@Tag(name = "CHAT BOT")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotLogController {
    ChatBotLogService chatBotLogService;

    @GetMapping("/frequent-questions")
    @Operation(summary = "Xem top 10 câu hỏi được hỏi nhiều nhất")
    public ApiResponse<List<FrequentQuestion>> getTop10FrequentQuestions() {
        List<FrequentQuestion> response = chatBotLogService.getTop10FrequentQuestions();
        return ApiResponseBuilder.buildSuccessResponse("Xem 10 câu hỏi được hỏi nhiều nhất", response);
    }
}
