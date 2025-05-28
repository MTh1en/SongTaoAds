package com.capstone.ads.repository.external;


import com.capstone.ads.dto.chatBot.ChatCompletionRequest;
import com.capstone.ads.dto.chatBot.ChatCompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openai-fine-tuning", url = "https://api.openai.com/v1")
public interface ChatBotRepository {
    @PostMapping(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE)
    ChatCompletionResponse getChatCompletions(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatCompletionRequest requestBody);
}
