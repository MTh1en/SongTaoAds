package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.FrequentQuestion;

import java.util.List;

public interface ChatBotLogService {
    List<FrequentQuestion> getTop10FrequentQuestions();
}
