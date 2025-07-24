package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.*;

import java.util.List;

public interface ChatBotService {
    String chat(ChatRequest request);

    String TestChat(TestChatRequest request);

    String translateToTextToImagePrompt(String prompt);

    ListModelsResponse getModels();

    List<FrequentQuestion> getTop10FrequentQuestions();
    ;

    ResponsePricingChat getModernBillboardPricing(ModernBillboardRequest request);

    ResponsePricingChat getTraditionalBillboardPricing(TraditionalBillboardRequest request);
}
