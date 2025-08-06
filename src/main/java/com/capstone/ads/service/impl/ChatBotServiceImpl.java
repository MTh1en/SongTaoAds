package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.event.ChatBotLogEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotClient;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ChatBotServiceImpl implements ChatBotService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;
    @Value("classpath:/prompt-templates/modern-pricing.st")
    private Resource modernPricingPromptTemplate;

    // Tạo prompt cho biển truyền thống từ template
    @Value("classpath:/prompt-templates/traditional-pricing.st")
    private Resource traditionalPricingPromptTemplate;

    private final ChatBotClient chatBotClient;
    private final SecurityContextUtils securityContextUtils;
    private final ModelChatBotRepository modelChatBotRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    public ChatBotServiceImpl(ChatBotClient chatBotClient, SecurityContextUtils securityContextUtils, ModelChatBotRepository modelChatBotRepository, ApplicationEventPublisher eventPublisher,
                              ChatClient.Builder chatClientBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.chatBotClient = chatBotClient;
        this.securityContextUtils = securityContextUtils;
        this.modelChatBotRepository = modelChatBotRepository;
        this.eventPublisher = eventPublisher;
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(20)
                .build();

        chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String chat(ChatRequest request) {
        String userId = securityContextUtils.getCurrentUserId();

        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        SystemMessage systemMessage = new SystemMessage("""
                Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo.
                """);
        UserMessage userMessage = new UserMessage(request.getPrompt());
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .build();

        Prompt prompt = new Prompt(systemMessage, userMessage);

        String response = chatClient
                .prompt(prompt)
                .options(openAiChatOptions)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, userId
                ))
                .call()
                .content();

        eventPublisher.publishEvent(new ChatBotLogEvent(
                this,
                response,
                request.getPrompt(),
                modelChatBot.getId()
        ));
        return response;
    }

    @Override
    public String TestChat(TestChatRequest request) {
        SystemMessage systemMessage = new SystemMessage("""
                Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo.
                """);
        UserMessage userMessage = new UserMessage(request.getPrompt());
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(request.getModel())
                .build();
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient
                .prompt(prompt)
                .options(openAiChatOptions)
                .call()
                .content();
    }

    @Override
    public String translateToTextToImagePrompt(String requirement) {
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        SystemMessage systemMessage = new SystemMessage("""
                You are an expert AI prompt engineer for Stable Diffusion, specializing in creating stunning and effective background images for billboards. Your task is to transform a user's high-level request into a detailed, descriptive, and actionable prompt suitable for image generation AI.
                        When generating the prompt, consider the following key aspects for a billboard background:
                           1.  Purpose & Mood: What is the general purpose or mood this background should convey (e.g., modern, classic, festive, natural, futuristic, calm, energetic)?
                           2.  Composition & Negative Space:** Emphasize areas for text/logo placement. Specify if large clear areas are needed (e.g., "ample negative space on the left/right/top/bottom for text overlays," "minimalist foreground," "uncluttered background").
                           3.  Visual Style: Describe the artistic style (e.g., photorealistic, abstract, watercolor, cinematic, minimalist, digital art), color palette (e.g., warm tones, cool tones, vibrant, muted, specific dominant colors), lighting (e.g., golden hour, neon glow, soft ambient light, dramatic), and overall aesthetic.
                           4.  Key Elements & Scene: Describe specific elements to include (e.g., city skyline, natural landscape, abstract shapes, subtle patterns, technological elements). Avoid elements that might distract from the main message of the billboard (e.g., overly complex details, prominent human faces unless specified).
                           5.  Quality & Details: Include terms for high quality (e.g., "high resolution," "ultra detailed," "8K," "sharp focus," "smooth rendering").
                        Your output should be a single, concise, and highly descriptive English prompt, ready to be used directly in Stable Diffusion. Do NOT include any conversational text, explanations, or formatting like bullet points or numbered lists. Only output the prompt.
                """);

        UserMessage userMessage = new UserMessage(requirement);
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .temperature(0.8)
                .build();
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient
                .prompt(prompt)
                .options(openAiChatOptions)
                .call()
                .content();
    }

    @Override
    public ListModelsResponse getModels() {
        return chatBotClient.getModels(
                "Bearer " + openaiApiKey
        );
    }

    @Override
    public List<String> getModernBillboardPricing(ModernBillboardRequest request) {
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        SystemMessage systemMessage = new SystemMessage("""
                Bạn là trợ lý AI chuyên tư vấn về thiết kế và in ấn biển quảng cáo.
                Hãy cung cấp báo giá chi tiết cho biển quảng cáo truyền thống dựa trên các thông số được cung cấp,
                bao gồm chi phí vật liệu, thiết kế, và lắp đặt.
                Trả lời bằng tiếng Việt và cung cấp giá ước tính rõ ràng theo định dạng được yêu cầu.
                """);

        PromptTemplate promptTemplate = new PromptTemplate(modernPricingPromptTemplate);

        Message userMessage = promptTemplate.createMessage(Map.of(
                "frame", request.getFrame() != null ? request.getFrame() : "Không có",
                "background", request.getBackground() != null ? request.getBackground() : "Không có",
                "border", request.getBorder() != null ? request.getBorder() : "Không có",
                "textAndLogo", request.getTextAndLogo() != null ? request.getTextAndLogo() : "Không có",
                "textSpecification", request.getTextSpecification() != null ? request.getTextSpecification() : "Không có",
                "installationMethod", request.getInstallationMethod() != null ? request.getInstallationMethod() : "Không có",
                "height", request.getHeight(),
                "width", request.getWidth(),
                "textSize", request.getTextSize()
        ));

        Prompt prompt = new Prompt(systemMessage, userMessage);

        log.info("User Modern: {}", prompt.getUserMessage());
        log.info("System Modern: {}", prompt.getSystemMessage());

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .maxTokens(500)
                .temperature(0.7)
                .build();

        String responseContent = chatClient
                .prompt(prompt)
                .options(options)
                .call()
                .content();

        return Stream.of(responseContent.split("\n"))
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTraditionalBillboardPricing(TraditionalBillboardRequest request) {
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        SystemMessage systemMessage = new SystemMessage("""
                Bạn là trợ lý AI chuyên tư vấn về thiết kế và in ấn biển quảng cáo.
                Hãy cung cấp báo giá chi tiết cho biển quảng cáo truyền thống dựa trên các thông số được cung cấp,
                bao gồm chi phí vật liệu, thiết kế, và lắp đặt.
                Trả lời bằng tiếng Việt và cung cấp giá ước tính rõ ràng theo định dạng được yêu cầu.
                """);

        PromptTemplate promptTemplate = new PromptTemplate(traditionalPricingPromptTemplate);


        Message userMessage = promptTemplate.createMessage(Map.of(
                "frame", request.getFrame() != null ? request.getFrame() : "Không có",
                "background", request.getBackground() != null ? request.getBackground() : "Không có",
                "border", request.getBorder() != null ? request.getBorder() : "Không có",
                "numberOfFaces", request.getNumberOfFaces(),
                "installationMethod", request.getInstallationMethod() != null ? request.getInstallationMethod() : "Không có",
                "billboardFace", request.getBillboardFace() != null ? request.getBillboardFace() : "Không có",
                "height", request.getHeight(),
                "width", request.getWidth()
        ));
        Prompt prompt = new Prompt(systemMessage, userMessage);

        log.info("User: {}", prompt.getUserMessage());
        log.info("System: {}", prompt.getSystemMessage());

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .maxTokens(500)
                .temperature(0.7)
                .build();

        String responseContent = chatClient
                .prompt(prompt)
                .options(options)
                .call()
                .content();

        return Stream.of(responseContent.split("\n"))
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList());
    }
}