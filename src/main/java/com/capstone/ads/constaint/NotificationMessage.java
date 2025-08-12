package com.capstone.ads.constaint;

public class NotificationMessage {
    public static String DEFAULT = "%s: %s";
    public static String PRICING_REJECTED = "%s: Khách hàng đã từ chối báo giá và đã offer giá khác, hãy check lại!";
    public static String ASSIGN_DESIGNER = "%s: Đã được giao cho bạn";
    public static String DESIGNER_REJECTED = "%s: Designer %s đã từ chối yêu cầu, hãy giao cho Designer khác";
    public static String CUSTOM_DESIGN_REQUEST_DEPOSITED = "%s: Đã đặt cọc hãy phân công cho Designer";
    public static String CUSTOM_DESIGN_REQUEST_FULLY_PAID = "%s: Đã thanh toán hết hãy gửi bản final";
    public static String DEMO_DESIGN_REJECTED = "%s: Bản demo đã bị từ chối, hãy gửi lại 1 bản khác";
    public static String CONTRACT_SINGED = "%s: Hợp đồng đã được khách hàng ký. Vui lòng xác nhận";
    public static String ORDER_DEPOSITED = "%s: Đơn hàng đã được đặt cọc, hãy báo ngày giao dự tính và đơn vị vận chuyển";
    public static String NEW_FEEDBACK = "%s: Khách hàng đã gửi phản hồi";
    public static String ANSWERED_FEEDBACK = "%s: Công ty đã trả lời phản hồi";
    public static String NEW_TICKET = "%s: Khách hàng đã gửi 1 yêu cầu hỗ trợ";
    public static String ANSWERED_TICKET = "%s: Yêu cầu hồ trợ đã được phản hồi";

    private NotificationMessage() {
    }
}
