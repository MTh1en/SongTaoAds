package com.capstone.ads.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // ============ AUTHENTICATION & AUTHORIZATION ============
    ACCOUNT_NOT_REGISTERED("Tài khoản chưa được đăng ký", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("Email hoặc mật khẩu không hợp lệ", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("Tài khoản chưa xác thực", HttpStatus.FORBIDDEN),
    ACCOUNT_BANNED("Tài khoản đã bị cấm", HttpStatus.FORBIDDEN),
    ACCOUNT_VERIFIED("Tài khoản đã được xác thực", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("Refresh token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("Truy cập bị từ chối", HttpStatus.FORBIDDEN),
    INVALID_TOKEN("Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND("Không tìm thấy vai trò", HttpStatus.NOT_FOUND),
    INVALID_PRINCIPAL("Principal không hợp lệ", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_AUTHORIZED("Vai trò không được ủy quyền", HttpStatus.UNAUTHORIZED),

    // ============ USER MANAGEMENT ============
    CUSTOMER_DETAIL_NOT_FOUND("Không tìm thấy chi tiết khách hàng", HttpStatus.NOT_FOUND),

    // ============ PRODUCT RELATED ============
    PRODUCT_TYPE_NOT_FOUND("Không tìm thấy loại biển hiệu", HttpStatus.NOT_FOUND),
    PRODUCT_TYPE_SIZE_NOT_FOUND("Không tìm thấy kích thước của loại biển hiệu", HttpStatus.NOT_FOUND),
    SIZE_NOT_FOUND("Không tìm thấy kích thước", HttpStatus.NOT_FOUND),
    SIZE_NOT_BELONG_PRODUCT_TYPE("Kích thước không thuộc loại biển hiệu này", HttpStatus.BAD_REQUEST),
    SIZE_VALUE_OUT_OF_RANGE("Giá trị kích thước nằm ngoài phạm vi", HttpStatus.BAD_REQUEST),

    // ============ ATTRIBUTE RELATED ============
    ATTRIBUTE_NOT_FOUND("Không tìm thấy thuộc tính", HttpStatus.NOT_FOUND),
    ATTRIBUTE_VALUE_NOT_FOUND("Không tìm thấy giá trị thuộc tính", HttpStatus.NOT_FOUND),
    ATTRIBUTE_NOT_BELONG_PRODUCT_TYPE("Thuộc tính không thuộc loại biển hiệu", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_NOT_BELONG_CUSTOMER_CHOICE_DETAIL("Thuộc tính không nằm trong lựa chọn của khách hàng", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_EXISTED_IN_CUSTOMER_CHOICES_DETAIL("Thuộc tính đã tồn tại trong lựa chọn của khách hàng", HttpStatus.BAD_REQUEST),

    // ============ CUSTOMER CHOICES ============
    CUSTOMER_CHOICES_NOT_FOUND("Không tìm thấy lựa chọn của khách hàng", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_SIZE_NOT_FOUND("Không tìm thấy kích thước khách hàng đã chọn", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_DETAIL_NOT_FOUND("Không tìm thấy thuộc tính khách hàng đã chọn", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICE_SIZE_EXISTED("Kích thước đã được chọn", HttpStatus.BAD_REQUEST),

    // ============ DESIGN RELATED ============
    DESIGNER_NOT_FOUND("Không tìm thấy nhà thiết kế", HttpStatus.NOT_FOUND),
    AI_DESIGN_NOT_FOUND("Không tìm thấy thiết kế AI", HttpStatus.NOT_FOUND),
    DESIGN_TEMPLATE_NOT_FOUND("Không tìm thấy mẫu thiết kế", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_DEPOSITED_NOT_FOUND("Không tìm thấy yêu cầu thiết kế tùy chỉnh đã được đặt cọc", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_NOT_FOUND("Không tìm thấy yêu cầu thiết kế tùy chỉnh", HttpStatus.NOT_FOUND),
    INVALID_CUSTOM_DESIGN_REQUEST_STATUS_TRANSITION("Thay đổi trạng thái yêu cầu thiết không phù hợp", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOM_DESIGN_STATUS_TRANSITION("Thay đổi trạng thái demo không phù hợp", HttpStatus.BAD_REQUEST),
    DEMO_DESIGN_NOT_FOUND("Không tìm thấy thiết kế mẫu", HttpStatus.NOT_FOUND),
    DEMO_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER("Thiết kế mẫu đang chờ quyết định từ khách hàng", HttpStatus.BAD_REQUEST),
    BACKGROUND_NOT_FOUND("Không tìm thấy background", HttpStatus.NOT_FOUND),
    ICON_NOT_FOUND("Không tìm thấy icon", HttpStatus.NOT_FOUND),

    // ============ ORDER & PAYMENT ============
    ORDER_NOT_FOUND("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND("Không tìm thấy chi tiết đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_ALREADY_EXISTS("Chi tiết đơn hàng đã tồn tại", HttpStatus.BAD_REQUEST),
    CONFLICTING_DESIGN_SOURCES("Chi tiết đơn hàng không thể được tạo từ cả yêu cầu thiết kế tùy chỉnh và lựa chọn của khách hàng cùng một lúc.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS_TRANSITION("Thay đổi trạng thái đơn hàng không hợp lệ", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND("Payment not found", HttpStatus.NOT_FOUND),
    INVALID_PROGRESS_LOG_STATUS("Trạng thái nhật ký tiến độ không hợp lệ (chỉ có trong PRODUCTION, PRODUCTION COMPLETED, DELIVERING, INSTALLED)", HttpStatus.BAD_REQUEST),

    // ============ CALCULATION & FORMULA ============
    INVALID_FORMULA("Công thức không hợp lệ", HttpStatus.BAD_REQUEST),
    CALCULATION_FAILED("Tính toán thất bại", HttpStatus.BAD_REQUEST),
    MISSING_SIZE_VALUE("Thiếu giá trị cho kích thước", HttpStatus.BAD_REQUEST),
    INVALID_VARIABLE_VALUE("Giá trị biến cho kích thước không hợp lệ", HttpStatus.BAD_REQUEST),

    // ============ FILE PROCESSING ============
    FILE_REQUIRED("Yêu cầu tệp tin", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("Không tìm thấy tệp tin", HttpStatus.NOT_FOUND),

    // ============ TICKET SYSTEM ============
    TICKET_NOT_FOUND("Không tìm thấy ticket", HttpStatus.NOT_FOUND),
    TICKET_NOT_OPEN("Ticket không được mở", HttpStatus.BAD_REQUEST),
    INVALID_SEVERITY("Mức độ nghiêm trọng của ticket không hợp lệ", HttpStatus.BAD_REQUEST),

    // ============ BUSINESS ENTITIES ============
    PRICE_PROPOSAL_NOT_FOUND("Không tìm thấy đề xuất giá", HttpStatus.NOT_FOUND),
    CONTRACT_NOT_FOUND("Không tìm thấy hợp đồng", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("Không tìm thấy phản hồi", HttpStatus.NOT_FOUND),
    COST_TYPE_NOT_FOUND("Không tìm thấy loại chi phí", HttpStatus.NOT_FOUND),
    CONTRACTOR_NOT_FOUND("Không tìm thấy nhà thầu", HttpStatus.NOT_FOUND),
    CORE_COST_TYPE_EXISTED("Đã tồn tại giá trị core trong loại biển hiệu", HttpStatus.BAD_REQUEST),

    // ============ EXTERNAL SERVICES ============
    EXTERNAL_SERVICE_ERROR("Lỗi dịch vụ bên ngoài", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("Đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    EXTERNAL_SERVICE_BAD_REQUEST("Yêu cầu dịch vụ bên ngoài không hợp lệ", HttpStatus.BAD_REQUEST),
    EXTERNAL_SERVICE_UNAVAILABLE("Dịch vụ bên ngoài không khả dụng", HttpStatus.SERVICE_UNAVAILABLE),
    STABLE_DIFFUSION_SERVER_NOT_AVAILABLE("Máy chủ Stable Diffusion không khả dụng", HttpStatus.SERVICE_UNAVAILABLE),

    // ============ AI ============
    MODEL_CHAT_NOT_FOUND("Không tìm thấy mô hình chat", HttpStatus.NOT_FOUND),
    TOPIC_NOT_FOUND("Không tìm thấy chủ đề", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND("Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND),
    CHAT_BOT_TOPIC_NOT_FOUND("Không tìm thấy chủ đề của model chat bot", HttpStatus.NOT_FOUND),

    // ============  NOTIFICATION ============
    NOTIFICATION_NOT_FOUND("Không tìm thấy thông báo", HttpStatus.NOT_FOUND),

   ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
