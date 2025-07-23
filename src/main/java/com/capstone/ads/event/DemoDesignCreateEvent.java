package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoDesignCreateEvent extends ApplicationEvent {
    private String customDesignRequestId;
    private boolean isNeedSupport;

    public DemoDesignCreateEvent(Object source, String customDesignRequestId, boolean isNeedSupport) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
        this.isNeedSupport = isNeedSupport;
    }
}
