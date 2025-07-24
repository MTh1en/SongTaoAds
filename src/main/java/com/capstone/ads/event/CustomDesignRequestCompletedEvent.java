package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignRequestCompletedEvent extends ApplicationEvent {
    private String customDesignRequestId;

    public CustomDesignRequestCompletedEvent(Object source, String customDesignRequestId) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
    }
}
