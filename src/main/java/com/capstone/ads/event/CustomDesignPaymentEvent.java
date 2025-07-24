package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignPaymentEvent extends ApplicationEvent {
    private String customDesignRequestId;
    private Boolean isDeposit;

    public CustomDesignPaymentEvent(Object source, String customDesignRequestId, Boolean isDeposit) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
        this.isDeposit = isDeposit;
    }
}
