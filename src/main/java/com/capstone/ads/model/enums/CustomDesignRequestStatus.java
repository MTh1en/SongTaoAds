package com.capstone.ads.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomDesignRequestStatus {
    PENDING("Yêu cầu thiết kế đang chờ xử lý"),
    PRICING_NOTIFIED("Đã có báo giá, hãy xem và phản hồi nhé!"),
    REJECTED_PRICING("Đã từ chối báo giá"),
    APPROVED_PRICING("Bạn đã duyệt báo giá. Vui lòng tiến hành đặt cọc để bắt đầu"),
    DEPOSITED("Bạn đã đặt cọc thành công cho yêu cầu thiết kế"),
    ASSIGNED_DESIGNER("Yêu cầu đã được giao cho thiết kế viên"),
    PROCESSING("Đang trong quá trình thiết kế"),
    DESIGNER_REJECTED("Đã từ chối yêu cầu thiết kế"),
    DEMO_SUBMITTED("Bản thiết kế mẫu đã được gửi cho bạn. Vui lòng kiểm tra"),
    REVISION_REQUESTED("Chúng tôi đã nhận được yêu cầu chỉnh sửa thiết kế"),
    WAITING_FULL_PAYMENT("Thiết kế đã hoàn thành. Vui lòng thanh toán phần còn lại"),
    FULLY_PAID("Bạn đã thanh toán đầy đủ"),
    COMPLETED("Bản final đã được gửi"),
    CANCELLED("Yêu cầu đã bị hủy");

    private final String message;
}
