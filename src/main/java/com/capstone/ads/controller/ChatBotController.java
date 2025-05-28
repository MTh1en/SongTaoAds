package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.ChatCompletionRequest;
import com.capstone.ads.dto.chatBot.ChatRequest;
import com.capstone.ads.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.chat(request.getPrompt());
        return ApiResponse.<String>builder()
                .message("Chat response retrieved successfully.")
                .result(reply)
                .build();
    }


}
