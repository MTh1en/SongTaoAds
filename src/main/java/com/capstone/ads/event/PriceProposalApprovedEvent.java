package com.capstone.ads.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceProposalApprovedEvent extends ApplicationEvent {
    private String customDesignRequestId;
    private Long totalPrice;
    private Long depositAmount;
    private Long remainingAmount;

    public PriceProposalApprovedEvent(Object source, String customDesignRequestId, Long totalPrice, Long depositAmount, Long remainingAmount) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
        this.totalPrice = totalPrice;
        this.depositAmount = depositAmount;
        this.remainingAmount = remainingAmount;
    }
}
