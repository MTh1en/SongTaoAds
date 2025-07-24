package com.capstone.ads.event;

import com.capstone.ads.model.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusChangedEvent extends ApplicationEvent {
    private String orderId;
    private OrderStatus orderStatus;
    private String userId;
    private List<String> imageUrls;

    public OrderStatusChangedEvent(Object source, String orderId, OrderStatus orderStatus, String userId, List<String> imageUrls) {
        super(source);
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.imageUrls = imageUrls != null ? imageUrls : Collections.emptyList();
    }
}
