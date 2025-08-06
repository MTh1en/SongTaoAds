package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.FrequentQuestion;
import com.capstone.ads.event.ChatBotLogEvent;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.ChatBotLogRepository;
import com.capstone.ads.service.ChatBotLogService;
import com.capstone.ads.service.ModelChatService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotLogServiceImpl implements ChatBotLogService {
    SecurityContextUtils securityContextUtils;
    ChatBotLogRepository chatBotLogRepository;
    ModelChatService modelChatService;

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @EventListener
    @Transactional
    public void createChatBotLog(ChatBotLogEvent chatBotLogEvent) {
        Users user = securityContextUtils.getCurrentUser();
        ModelChatBot modelChatBot = modelChatService.getModelChatBotById(chatBotLogEvent.getModelChatBotId());

        ChatBotLog log = ChatBotLog.builder()
                .answer(chatBotLogEvent.getResponse())
                .question(chatBotLogEvent.getQuestion())
                .modelChatBot(modelChatBot)
                .users(user)
                .build();
        chatBotLogRepository.save(log);
    }

    @Override
    public List<FrequentQuestion> getTop10FrequentQuestions() {
        List<FrequentQuestion> questions = chatBotLogRepository.findTop10FrequentQuestions();
        return questions.stream().limit(10).collect(Collectors.toList());
    }
}
