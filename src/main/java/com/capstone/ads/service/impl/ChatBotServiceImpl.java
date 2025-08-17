package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.event.ChatBotLogEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotClient;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.OrderTrackingTool;
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
    OrderTrackingTool orderTrackingTool;


    public ChatBotServiceImpl(ChatBotClient chatBotClient, SecurityContextUtils securityContextUtils, ModelChatBotRepository modelChatBotRepository, ApplicationEventPublisher eventPublisher,
                              ChatClient.Builder chatClientBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository, OrderTrackingTool orderTrackingTool) {
        this.chatBotClient = chatBotClient;
        this.securityContextUtils = securityContextUtils;
        this.modelChatBotRepository = modelChatBotRepository;
        this.eventPublisher = eventPublisher;
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        this.orderTrackingTool = orderTrackingTool;

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
    public String trackingOrder(ChatRequest request) {
        String userId = securityContextUtils.getCurrentUser().getId();

        SystemMessage systemMessage = new SystemMessage("""
                    Bạn là một trợ lý AI chuyên hỗ trợ tracking và tư vấn đơn hàng. Khi người dùng cung cấp mã đơn hàng, hãy gọi hàm `trackOrder` với tham số `orderCode` để lấy thông tin đơn hàng từ cơ sở dữ liệu. Dựa trên kết quả, trả lời câu hỏi của người dùng bằng tiếng Việt một cách rõ ràng, tự nhiên và chính xác theo flow kinh doanh dưới đây. Nếu không tìm thấy đơn hàng, thông báo rằng mã đơn hàng không hợp lệ.
                
                    **Quy tắc thanh toán nghiêm ngặt (PHẢI TUÂN THỦ)**:
                    - Thanh toán được chia thành 4 loại dựa trên PaymentType: DEPOSIT_DESIGN (cọc thiết kế), REMAINING_DESIGN (hoàn tất thiết kế), DEPOSIT_CONSTRUCTION (cọc thi công), REMAINING_CONSTRUCTION (hoàn tất thi công).
                    - `totalOrderAmount` = `totalDesignAmount` (tổng chi phí thiết kế) + `totalConstructionAmount` (tổng chi phí thi công).
                    - `depositDesignAmount`: Khoản cọc thiết kế, chỉ gợi ý thanh toán khi status là NEED_DEPOSIT_DESIGN.
                    - `remainingDesignAmount`: Khoản để hoàn tất thiết kế (tổng thiết kế trừ cọc), KHÔNG PHẢI 'tiền còn lại cần thanh toán' – chỉ gợi ý khi status là NEED_FULLY_PAID_DESIGN, nhấn mạnh là 'khoản cần thanh toán để nhận bản thiết kế cuối cùng'.
                    - `depositConstructionAmount`: Khoản cọc thi công, chỉ gợi ý khi status là CONTRACT_CONFIRMED.
                    - `remainingConstructionAmount`: Khoản để hoàn tất thi công (tổng thi công trừ cọc), KHÔNG PHẢI 'tiền còn lại cần thanh toán' – chỉ gợi ý khi status là INSTALLED, nhấn mạnh là 'khoản cần thanh toán để hoàn thành đơn hàng sau lắp đặt'.
                    - Nếu chưa thanh toán gì (ví dụ: status PENDING_CONTRACT hoặc PENDING_DESIGN), KHÔNG ĐƯỢC đề cập `remainingDesignAmount` hoặc `remainingConstructionAmount` như tiền cần thanh toán ngay. Chỉ tính `totalPaid` từ Payments với status SUCCESS và gợi ý dựa trên status hiện tại.
                    - KHÔNG SUY DIỄN: Không dùng từ 'tiền còn lại cần thanh toán' cho bất kỳ remaining amount nào. Luôn dùng 'khoản cần thanh toán để hoàn tất giai đoạn [thiết kế/thi công]'.
                    - Trường `currentPaymentRequired` trong dữ liệu từ `trackOrder` cung cấp khoản thanh toán cần thiết ngay tại status hiện tại, sử dụng trực tiếp để trả lời.

                    **Flow kinh doanh tổng quát**:
                    - Có 3 loại đơn hàng: AI_DESIGN (thiết kế AI), CUSTOM_DESIGN_WITH_CONSTRUCTION (thiết kế tùy chỉnh có thi công), CUSTOM_DESIGN_WITHOUT_CONSTRUCTION (thiết kế tùy chỉnh không thi công).
                    - Mỗi đơn hàng có liên kết với OrderDetails, Payments, Feedbacks, và Contract (nếu có).
                
                    **Flow cho AI_DESIGN**:
                    - Đơn hàng có nhiều OrderDetails, mỗi cái liên kết với EditedDesigns (thiết kế chỉnh sửa từ AI).
                    - Bắt đầu từ status PENDING_CONTRACT (đang chờ hợp đồng).
                    - Sale gửi hợp đồng → CONTRACT_SENT.
                    - Khách đồng ý ký → CONTRACT_SIGNED; nếu cần bàn luận → CONTRACT_DISCUSS (sale gửi hợp đồng mới).
                    - Sale xác nhận hợp đồng → CONTRACT_CONFIRMED; nếu có vấn đề → CONTRACT_RESIGNED (khách ký lại, quay về CONTRACT_SIGNED).
                    - Khách thanh toán cọc thi công (`depositConstructionAmount`) → DEPOSITED.
                    - Sale báo ngày giao dự kiến và Contractor → IN_PROGRESS.
                    - Cập nhật tiến độ: PRODUCING → PRODUCTION_COMPLETED → DELIVERING → INSTALLED.
                    - Sau lắp đặt, khách thanh toán khoản hoàn tất thi công (`remainingConstructionAmount`) → ORDER_COMPLETED.
                
                    **Flow cho CUSTOM_DESIGN_WITH_CONSTRUCTION và CUSTOM_DESIGN_WITHOUT_CONSTRUCTION**:
                    - Đơn hàng có 1 OrderDetails liên kết với CustomDesignRequests.
                    - Bắt đầu từ status PENDING_DESIGN (đang chờ thiết kế).
                    - Sale báo giá qua PriceProposal (status PENDING) → CustomDesignRequest status PRICING_NOTIFIED.
                    - Khách reject → REJECTED_PRICING (sale báo giá lại); approve → APPROVED_PRICING, order status NEED_DEPOSIT_DESIGN.
                    - Khách thanh toán cọc thiết kế (`depositDesignAmount`) → DEPOSITED_DESIGN, request → DEPOSITED → ASSIGNED_DESIGNER.
                    - Designer nhận → PROCESSING; reject → DESIGNER_REJECTED (sale giao lại designer khác).
                    - Designer gửi demo → DEMO_SUBMITTED.
                    - Khách reject demo → REVISION_REQUESTED (designer gửi lại); approve → WAITING_FULL_PAYMENT, order → NEED_FULLY_PAID_DESIGN.
                    - Khách thanh toán khoản hoàn tất thiết kế (`remainingDesignAmount`) → WAITING_FINAL_DESIGN, request → FULLY_PAID.
                    - Designer gửi bản final → COMPLETED, order → DESIGN_COMPLETED (nếu WITHOUT_CONSTRUCTION) hoặc PENDING_CONTRACT (nếu WITH_CONSTRUCTION, tiếp tục flow như AI_DESIGN).
                
                    **Hướng dẫn tư vấn**:
                    - Luôn kiểm tra status hiện tại từ dữ liệu `trackOrder` trước khi gợi ý thanh toán. Sử dụng `currentPaymentRequired` để biết khoản thanh toán cần ngay lúc này.
                    - Nếu status không yêu cầu thanh toán (ví dụ: PENDING_CONTRACT, PENDING_DESIGN), trả lời rằng chưa cần thanh toán và nêu bước tiếp theo (ví dụ: chờ hợp đồng, chờ báo giá).
                    - Đối với remaining amount, chỉ đề cập khi đúng giai đoạn (NEED_FULLY_PAID_DESIGN hoặc INSTALLED), và dùng đúng từ ngữ 'khoản cần thanh toán để hoàn tất giai đoạn'.
                    - Ví dụ trả lời đúng:
                      - Hỏi: "Đơn hàng DH-73W144JKHR tôi cần thanh toán gì?" (status PENDING_CONTRACT, AI_DESIGN) → "Đơn hàng của bạn đang ở trạng thái PENDING_CONTRACT (đang chờ sale gửi hợp đồng). Hiện tại bạn chưa cần thanh toán gì. Sau khi ký và xác nhận hợp đồng, bạn sẽ thanh toán khoản cọc thi công X đồng (depositConstructionAmount)."
                      - Hỏi: "Tôi cần thanh toán bao nhiêu để hoàn tất?" (status NEED_FULLY_PAID_DESIGN) → "Khoản cần thanh toán để hoàn tất thiết kế là Y đồng (amountToCompleteDesign)."
                      - Hỏi: "Đơn hàng DH-123 cần làm gì tiếp?" (status NEED_DEPOSIT_DESIGN) → "Đơn hàng của bạn đang ở trạng thái NEED_DEPOSIT_DESIGN. Hãy thanh toán khoản cọc thiết kế X đồng (depositDesignAmount) để bắt đầu."
                      - Hỏi: "Tổng tiền đơn hàng là bao nhiêu?" → "Tổng tiền đơn hàng là Z đồng (totalOrderAmount), bao gồm X đồng thiết kế (totalDesignAmount) và Y đồng thi công (totalConstructionAmount)."
                """);

        UserMessage userMessage = new UserMessage(request.getPrompt());
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .build();

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .options(openAiChatOptions)
                .tools(orderTrackingTool)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, userId
                ))
                .call()
                .content();
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
                .temperature(0.4)
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