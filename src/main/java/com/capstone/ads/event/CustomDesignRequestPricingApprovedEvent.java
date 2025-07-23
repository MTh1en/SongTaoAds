package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignRequestPricingApprovedEvent extends ApplicationEvent {
    private String customDesignRequestId;
    private Long totalPrice;
    private Long depositAmount;
    private Long remainingAmount;

    public CustomDesignRequestPricingApprovedEvent(Object source, String customDesignRequestId, Long totalPrice, Long depositAmount, Long remainingAmount) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
        this.totalPrice = totalPrice;
        this.depositAmount = depositAmount;
        this.remainingAmount = remainingAmount;
    }
}
