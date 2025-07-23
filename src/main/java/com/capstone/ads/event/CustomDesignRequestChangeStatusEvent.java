package com.capstone.ads.event;

import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignRequestChangeStatusEvent extends ApplicationEvent {
    private String customDesignRequestId;
    private CustomDesignRequestStatus status;

    public CustomDesignRequestChangeStatusEvent(Object source, String customDesignRequestId, CustomDesignRequestStatus status) {
        super(source);
        this.customDesignRequestId = customDesignRequestId;
        this.status = status;
    }
}
