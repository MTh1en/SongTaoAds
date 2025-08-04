package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.ChatCompletionRequest;
import com.capstone.ads.dto.chatBot.ChatCompletionResponse;
import com.capstone.ads.dto.user_response.UserResponseDTO;
import com.capstone.ads.dto.user_response.UserResponseRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UserResponseMapper;
import com.capstone.ads.model.Conversation;
import com.capstone.ads.model.Question;
import com.capstone.ads.model.UserResponse;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ConversationRepository;
import com.capstone.ads.repository.internal.QuestionRepository;
import com.capstone.ads.repository.internal.UserResponseRepository;
import com.capstone.ads.service.UserResponseService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserResponseServiceImpl implements UserResponseService {
    private final UserResponseRepository userResponseRepository;
    private final QuestionRepository questionRepository;
    private final ConversationRepository conversationRepository;
    private final UserResponseMapper userResponseMapper;
    private final ConversationRepository convRepository;
    private final SecurityContextUtils securityContextUtils;
    private final ChatBotRepository chatBotRepository;

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    @Override
    public UserResponseDTO createUserResponse(String questionId, String conversationId, UserResponseRequest userResponseRequest) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        UserResponse userResponse = userResponseMapper.toEntity(userResponseRequest);
        userResponse.setQuestion(question);
        userResponse.setConversation(conversation);
        UserResponse savedUserResponse = userResponseRepository.save(userResponse);
        return userResponseMapper.toDto(savedUserResponse);
    }

    @Override
    public List<UserResponseDTO> getAllUserResponses() {
        return userResponseRepository.findAll().stream()
                .map(userResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserResponseById(String id) {
        return userResponseRepository.findById(id)
                .map(userResponseMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.USER_RESPONSE_NOT_FOUND));
    }

    @Override
    public List<UserResponseDTO> getUserResponsesByQuestionId(String questionId) {
        return userResponseRepository.findByQuestion_Id(questionId).stream()
                .map(userResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUserResponsesByConversationId(String conversationId) {
        return userResponseRepository.findByConversation_Id(conversationId).stream()
                .map(userResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserResponse(String id, UserResponseDTO userResponseDTO) {
        UserResponse existingUserResponse = userResponseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_RESPONSE_NOT_FOUND));
        existingUserResponse.setAnswer(userResponseDTO.getAnswer());
        UserResponse updatedUserResponse = userResponseRepository.save(existingUserResponse);
        return userResponseMapper.toDto(updatedUserResponse);
    }

    @Override
    public void deleteUserResponse(String id) {
        UserResponse userResponse = userResponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User response not found with id: " + id));
        userResponseRepository.delete(userResponse);
    }


    @Transactional
    @Override
    public List<String> generatePricingQuotePrompt(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        if (conversation.getModelChatBot() == null) {
            throw new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND);
        }

        // Fetch all questions and responses for the topic
        List<Question> questions = questionRepository.findByTopic_Id(conversation.getTopic().getId());
        List<UserResponse> userResponses = userResponseRepository.findByConversation_Id(conversation.getId());

        // Build the prompt with all questions and their responses
        StringBuilder prompt = new StringBuilder();
        prompt.append("Dựa trên cuộc trò chuyện hoàn chỉnh với các thông tin sau: ");
        //prompt.append("Giá báo giá hiện tại: ").append(conversation.getPriceQuote() != null ? String.join(", ", conversation.getPriceQuote()) : "0").append(" VND. ");
        prompt.append("Chủ đề: ").append(conversation.getTopic().getTitle()).append(". ");
        prompt.append("Tất cả các câu hỏi và câu trả lời của người dùng:\n");

        for (Question q : questions) {
            UserResponse response = userResponses.stream()
                    .filter(ur -> ur.getQuestion().getId().equals(q.getId()))
                    .findFirst()
                    .orElse(null);
            prompt.append("- Câu hỏi: ").append(q.getQuestion()).append(". ");
            prompt.append("Câu trả lời: ").append(response != null ? response.getAnswer() : "Chưa có câu trả lời").append(".\n");
        }

        prompt.append("Hãy cung cấp một báo giá mới dựa trên toàn bộ thông tin trên, áp dụng công thức: Tổng chi phí = (khung bảng + nền bảng + viền bảng) * (cao * ngang) + (chữ và logo + quy cách chữ) * (kích thước chữ) + (quy cách gắn). ");
        prompt.append("Bạn PHẢI trả lời bằng tiếng Việt và tuân thủ ĐẦY ĐỦ định dạng sau, ngay cả khi một số giá trị là 0 nếu không có dữ liệu:\n");
        prompt.append("- Chi phí Khung bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Nền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Viền bảng: [số tiền] VND\n");
        prompt.append("- Chi phí Chữ và logo: [số tiền] VND/m²\n");
        prompt.append("- Chi phí Quy cách chữ: [số tiền] VND\n");
        prompt.append("- Chi phí Quy cách gắn: [số tiền] VND\n");
        prompt.append("- Tổng chi phí: [số tiền] VND");

        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(conversation.getModelChatBot().getModelName());
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI chuyên tư vấn về thiết kế và in ấn biển quảng cáo. Hãy cung cấp báo giá chi tiết cho biển quảng cáo truyền thống dựa trên các thông số được cung cấp, bao gồm chi phí vật liệu, thiết kế, và lắp đặt. Trả lời bằng tiếng Việt và cung cấp giá ước tính rõ ràng theo định dạng được yêu cầu."),
                new ChatCompletionRequest.Message("user", prompt.toString())
        ));
dd
        // Query chatbot using chatBotRepository
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest
        );

        // Process the response
        String aiResponse = response.getChoices().getFirst().getMessage().getContent();
        List<String> pricingLines = aiResponse.lines()
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList());

        conversation.setPriceQuote(pricingLines);
        conversationRepository.save(conversation);

        return pricingLines;
    }



}
