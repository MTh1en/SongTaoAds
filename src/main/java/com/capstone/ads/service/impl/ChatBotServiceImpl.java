package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.ChatCompletionRequest;
import com.capstone.ads.dto.chatBot.ChatCompletionResponse;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatBotRepository openAiClient;
    private final String openaiApiKey;

    public ChatBotServiceImpl(ChatBotRepository openAiClient,
                              @Value("${spring.ai.openai.api-key}") String openaiApiKey) {
        this.openAiClient = openAiClient;
        this.openaiApiKey = openaiApiKey;
    }

    public String chat(String prompt) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel("ft:gpt-4.1-mini-2025-04-14:songtaoadsai:songtao-ai-premium:BbpNfDHX");
        request.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", prompt)
        ));

        ChatCompletionResponse response = openAiClient.getChatCompletions(
                "Bearer " + openaiApiKey,
                request
        );

        return response.getChoices().get(0).getMessage().getContent();

    }
}