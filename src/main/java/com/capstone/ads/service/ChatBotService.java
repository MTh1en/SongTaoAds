package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.*;

import java.util.List;

public interface ChatBotService {
    String chat(ChatRequest request);

    String trackingOrder(ChatRequest request);

    String TestChat(TestChatRequest request);

    String translateToTextToImagePrompt(String prompt);

    ListModelsResponse getModels();

    ModernBillboardResponse getModernBillboardPricing(ModernBillboardRequest request);

    TraditionalBillboardResponse getTraditionalBillboardPricing(TraditionalBillboardRequest request);
}
