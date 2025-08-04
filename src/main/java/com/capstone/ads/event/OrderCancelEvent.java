package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCancelEvent extends ApplicationEvent {
    String orderId;

    public OrderCancelEvent(Object source, String orderId) {
        super(source);
        this.orderId = orderId;
    }
}
