package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ChatBotLogMapper;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ChatBotLogRepository;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotServiceImpl implements ChatBotService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final ChatBotLogRepository chatBotLogRepository;
    private final SecurityContextUtils securityContextUtils;
    private final ChatBotRepository chatBotRepository;
    private final ModelChatBotRepository modelChatBotRepository;
    private final ChatBotLogMapper chatBotLogMapper;

    @Override
    public String chat(ChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        String modelChat = modelChatBot.getModelName();
        completionRequest.setModel(modelChat);
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        saveResponse(response, request.getPrompt(), modelChatBot);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String TestChat(TestChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(request.getModel());
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String translateToTextToImagePrompt(String prompt) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        completionRequest.setModel(modelChatBot.getModelName());
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Based on the customer's request, let write a prompt in English to createAttribute an image of the billboard. Note: Do not createAttribute an image, only the prompt in English in the answer in one line and no prefix like the answer is: ..."),
                new ChatCompletionRequest.Message("user", "The customer request is:" + prompt)
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public ListModelsResponse getModels(){
        return chatBotRepository.getModels(
                "Bearer " + openaiApiKey
        );
    }

    @Override
    public List<FrequentQuestion> getTop10FrequentQuestions() {
        List<FrequentQuestion> questions = chatBotLogRepository.findTop10FrequentQuestions();
        return questions.stream().limit(10).collect(Collectors.toList());
    }

    @Override
    public ResponsePricingChat getModernBillboardPricing(ModernBillboardRequest request) {

        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        String prompt = buildModernPricingPrompt(request);

        RequestPricingChat pricingRequest = new RequestPricingChat();
        pricingRequest.setModel(modelChatBot.getModelName());
        pricingRequest.setMessages(List.of(
                new RequestPricingChat.Message("system", "Bạn là trợ lý AI chuyên tư vấn về thiết kế và in ấn biển quảng cáo. Hãy cung cấp báo giá chi tiết cho biển quảng cáo truyền thống dựa trên các thông số được cung cấp, bao gồm chi phí vật liệu, thiết kế, và lắp đặt. Trả lời bằng tiếng Việt và cung cấp giá ước tính rõ ràng theo định dạng được yêu cầu."),
                new RequestPricingChat.Message("user", prompt)
        ));
        pricingRequest.setMaxTokens(500);
        pricingRequest.setTemperature(0.7);
        ResponsePricingChat response = chatBotRepository.getResponseChatCompletions(
                "Bearer " + openaiApiKey,
                pricingRequest);


        //saveResponse(response, prompt, modelChatBot);

        return response;
    }

    @Override
    public ResponsePricingChat getTraditionalBillboardPricing(TraditionalBillboardRequest request) {

        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));

        String prompt = buildTraditionalPricingPrompt(request);

        RequestPricingChat pricingRequest = new RequestPricingChat();
        pricingRequest.setModel(modelChatBot.getModelName());
        pricingRequest.setMessages(List.of(
                new RequestPricingChat.Message("system", "Bạn là trợ lý AI chuyên tư vấn về thiết kế và in ấn biển quảng cáo. Hãy cung cấp báo giá chi tiết cho biển quảng cáo truyền thống dựa trên các thông số được cung cấp, bao gồm chi phí vật liệu, thiết kế, và lắp đặt. Trả lời bằng tiếng Việt và cung cấp giá ước tính rõ ràng theo định dạng được yêu cầu."),
                new RequestPricingChat.Message("user", prompt)
        ));
        pricingRequest.setMaxTokens(500);
        pricingRequest.setTemperature(0.7);
        ResponsePricingChat response = chatBotRepository.getResponseChatCompletions(
                "Bearer " + openaiApiKey,
                pricingRequest);

        //saveResponse(response, prompt, modelChatBot);
        log.info("Generated prompt for traditional billboard pricing: {}", prompt);
        return response;
    }
    private String buildModernPricingPrompt(ModernBillboardRequest modern) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Cung cấp báo giá cho một biển quảng cáo hiện đại với các thông số sau: ");
        prompt.append("Loại biển: Hiện đại. ");
        if (modern.getFrame() != null && !modern.getFrame().trim().isEmpty()) {
            prompt.append("Khung bảng: ").append(modern.getFrame()).append(". ");
        }
        if (modern.getBackground() != null && !modern.getBackground().trim().isEmpty()) {
            prompt.append("Nền bảng: ").append(modern.getBackground()).append(". ");
        }
        if (modern.getBorder() != null && !modern.getBorder().trim().isEmpty()) {
            prompt.append("Viền bảng: ").append(modern.getBorder()).append(". ");
        }
        if (modern.getTextAndLogo() != null && !modern.getTextAndLogo().trim().isEmpty()) {
            prompt.append("Chữ và logo: ").append(modern.getTextAndLogo()).append(". ");
        }
        if (modern.getTextSpecification() != null && !modern.getTextSpecification().trim().isEmpty()) {
            prompt.append("Quy cách chữ: ").append(modern.getTextSpecification()).append(". ");
        }
        if (modern.getInstallationMethod() != null && !modern.getInstallationMethod().trim().isEmpty()) {
            prompt.append("Quy cách gắn: ").append(modern.getInstallationMethod()).append(". ");
        }
        prompt.append(String.format("Kích thước: Cao %.2f mét, Ngang %.2f mét. ", modern.getHeight(), modern.getWidth()));
        prompt.append(String.format("Kích thước chữ: %.2f cm. ", modern.getTextSize()));
        prompt.append("Vui lòng cung cấp báo giá chi tiết bằng tiếng Việt, bao gồm chi phí vật liệu, thiết kế, in ấn, và lắp đặt. ");
        prompt.append("Trả lời theo định dạng sau:\n");
        prompt.append("- Chi phí Khung bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Nền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Viền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Chữ và logo: [số tiền] VND/m²\n");
        prompt.append("- Chi phí Quy cách chữ: [số tiền] VND\n");
        prompt.append("- Chi phí Quy cách gắn: [số tiền] VND\n");
        prompt.append("- Tổng chi phí: [số tiền] VND");
        return prompt.toString();
    }

    private String buildTraditionalPricingPrompt(TraditionalBillboardRequest traditional) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Cung cấp báo giá cho một biển quảng cáo truyền thống với các thông số sau: ");
        prompt.append("Loại biển: Truyền thống. ");
        if (traditional.getFrame() != null && !traditional.getFrame().trim().isEmpty()) {
            prompt.append("Khung bảng: ").append(traditional.getFrame()).append(". ");
        }
        if (traditional.getBackground() != null && !traditional.getBackground().trim().isEmpty()) {
            prompt.append("Nền bảng: ").append(traditional.getBackground()).append(". ");
        }
        if (traditional.getBorder() != null && !traditional.getBorder().trim().isEmpty()) {
            prompt.append("Viền bảng: ").append(traditional.getBorder()).append(". ");
        }
        prompt.append("Số mặt: ").append(traditional.getNumberOfFaces()).append(". ");
        if (traditional.getInstallationMethod() != null && !traditional.getInstallationMethod().trim().isEmpty()) {
            prompt.append("Quy cách gắn: ").append(traditional.getInstallationMethod()).append(". ");
        }
        if (traditional.getBillboardFace() != null && !traditional.getBillboardFace().trim().isEmpty()) {
            prompt.append("Mặt bảng: ").append(traditional.getBillboardFace()).append(". ");
        }
        prompt.append(String.format("Kích thước: Cao %.2f mét, Ngang %.2f mét. "
                , traditional.getHeight(), traditional.getWidth()));
        prompt.append("Vui lòng cung cấp báo giá chi tiết bằng tiếng Việt, bao gồm chi phí vật liệu, thiết kế, in ấn, và lắp đặt. ");
        prompt.append("Trả lời theo định dạng sau:\n");
        prompt.append("- Chi phí Khung bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Nền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Viền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Mặt bảng: [số tiền] VND/m²\n");
        prompt.append("- Chi phí Số mặt bảng: \n");
        prompt.append("- Chi phí Quy cách gắn: [số tiền] VND\n");
        prompt.append("- Tổng chi phí: [số tiền] VND");
        return prompt.toString();
    }


    private void saveResponse(ChatCompletionResponse response, String question, ModelChatBot modelChat) {
        var user = securityContextUtils.getCurrentUser();
        // Extract answer from response
        String answer = (response.getChoices() != null && !response.getChoices().isEmpty())
                ? response.getChoices().getFirst().getMessage().getContent()
                : null;
        if (answer == null || answer.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        ChatBotLog log = chatBotLogMapper.toEntity(question, answer);
        log.setModelChatBot(modelChat);
        log.setUsers(user);
        chatBotLogRepository.save(log);
    }

}