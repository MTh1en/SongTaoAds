package com.capstone.ads.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING_DESIGN("Đang chờ thiết kế"),
    NEED_DEPOSIT_DESIGN("Cần thanh toán cọc thiết kế. Hãy thanh toán ngay"),
    DEPOSITED_DESIGN("Đã thanh toán cọc thiết kế"),
    NEED_FULLY_PAID_DESIGN("Cần thanh toán đầy đủ phí thiết kế"),
    WAITING_FINAL_DESIGN("Đang chờ thiết kế cuối cùng"),
    DESIGN_COMPLETED("Đã hoàn tất thiết kế"),

    PENDING_CONTRACT("Đang chờ hợp đồng"),
    CONTRACT_SENT("Hợp đồng đã được gửi"),
    CONTRACT_SIGNED("Hợp đồng đã được ký"),
    CONTRACT_DISCUSS("Hợp đồng đang được thảo luận"),
    CONTRACT_RESIGNED("Yêu cẩu gửi lại hợp đồng đã ký"),
    CONTRACT_CONFIRMED("Hợp đồng đã được xác nhận. Hãy đặt cọc ngay!"),

    DEPOSITED("Đã thanh toán cọc đơn hàng"),
    IN_PROGRESS("Đã cung cấp ngày giao dự tính, đơn vị thi công và chuẩn bị sản xuất"),
    PRODUCING("Đang thi công"),
    PRODUCTION_COMPLETED("Đã thi công xong và chuẩn bị giao hàng"),
    DELIVERING("Đang giao hàng"),
    INSTALLED("Đã lắp đặt xong"),
    ORDER_COMPLETED("Đơn hàng đã hoàn thành"),
    CANCELLED("Đơn hàng đã bị hủy");

    private final String message;
}
