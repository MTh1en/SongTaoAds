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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotServiceImpl implements ChatBotService {
    @NonFinal
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    @NonFinal
    @Value("classpath:/prompt-templates/modern-pricing.st")
    private Resource modernPricingPromptTemplate;

    @NonFinal
    @Value("classpath:/prompt-templates/traditional-pricing.st")
    private Resource traditionalPricingPromptTemplate;

    ChatBotClient chatBotClient;
    SecurityContextUtils securityContextUtils;
    ModelChatBotRepository modelChatBotRepository;
    ApplicationEventPublisher eventPublisher;
    ChatClient chatClient;
    JdbcChatMemoryRepository jdbcChatMemoryRepository;


    public ChatBotServiceImpl(ChatBotClient chatBotClient, SecurityContextUtils securityContextUtils, ModelChatBotRepository modelChatBotRepository, ApplicationEventPublisher eventPublisher,
                              ChatClient.Builder chatClientBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.chatBotClient = chatBotClient;
        this.securityContextUtils = securityContextUtils;
        this.modelChatBotRepository = modelChatBotRepository;
        this.eventPublisher = eventPublisher;
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

        chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String chat(ChatRequest request) {
        String userId = securityContextUtils.getCurrentUser().getId();

        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        SystemMessage systemMessage = new SystemMessage("""
                Bạn là một trợ lý ảo tư vấn khách hàng của công ty Song Tạo.
                Mục tiêu của bạn là cung cấp thông tin chính xác, hữu ích và thân thiện cho khách hàng.
                Bạn phải tuân thủ các nguyên tắc sau:
                1.  **Chào hỏi thân thiện:** Bắt đầu mọi cuộc trò chuyện bằng một lời chào hỏi ấm áp.
                2.  **Chính xác:** Chỉ cung cấp thông tin đã được xác minh từ nguồn nội bộ.
                3.  **Hữu ích:** Hướng dẫn khách hàng đến các giải pháp hoặc nguồn tài nguyên phù hợp.
                4.  **Thân thiện và chuyên nghiệp:** Duy trì một thái độ tích cực, lịch sự, và sử dụng ngôn ngữ rõ ràng, dễ hiểu.
                5.  **Không suy diễn:** Nếu bạn không biết câu trả lời, hãy nói rằng bạn không có thông tin và hướng dẫn khách hàng liên hệ với đội ngũ hỗ trợ trực tiếp.
                6.  **Tránh cung cấp thông tin cá nhân:** Không bao giờ hỏi hoặc yêu cầu thông tin nhạy cảm của khách hàng như mật khẩu, số thẻ tín dụng, thông tin để liên lạc sau, ...
                7.  **Kết thúc cuộc trò chuyện:** Khi vấn đề của khách hàng đã được giải quyết, hãy hỏi xem họ có cần hỗ trợ gì thêm không.
                8.  **Không đặt lịch, hứa hẹn:** Không yêu cầu khách hàng cung cấp thông tin cá nhân để liên lạc sau.
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
                You are an expert AI prompt engineer for Stable Diffusion, specializing in creating simple, clean, and effective background images for billboard advertisements.
                
                Your job is to:
                - Take the user's request and extract only the essential mood, color, and theme.
                - Remove or simplify any complex, busy, or crowded elements such as dense buildings, many people, heavy patterns, or overly detailed objects.
                - Always include clear instructions for large empty or uncluttered areas suitable for text/logo placement.
                - Keep the composition minimalistic, focusing on smooth gradients, soft textures, and simple shapes.
                - Always describe the artistic style, color palette, lighting, and atmosphere clearly.
                - Avoid anything that might distract from the main message of the billboard.
                - Never describe or request any text, lettering, typography, or logo in the image.
                
                Output only a single concise English prompt that can be used directly in Stable Diffusion. Do NOT explain or add formatting.
                
                At the end of the prompt, always append:
                "minimalist composition, ample negative space, uncluttered design, clean background"
                
                """);

        UserMessage userMessage = new UserMessage(requirement);
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.6)
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
    public ModernBillboardResponse getModernBillboardPricing(ModernBillboardRequest request) {
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

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .maxTokens(500)
                .temperature(0.7)
                .build();

        return chatClient
                .prompt(prompt)
                .options(options)
                .call()
                .entity(ModernBillboardResponse.class);
    }

    @Override
    public TraditionalBillboardResponse getTraditionalBillboardPricing(TraditionalBillboardRequest request) {
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

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelChatBot.getModelName())
                .maxTokens(500)
                .temperature(0.7)
                .build();

        return chatClient
                .prompt(prompt)
                .options(options)
                .call()
                .entity(TraditionalBillboardResponse.class);
    }

}