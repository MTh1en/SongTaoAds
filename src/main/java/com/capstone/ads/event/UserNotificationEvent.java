package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserNotificationEvent extends ApplicationEvent {
    private String userId;
    private String message;

    public UserNotificationEvent(Object source, String userId, String message) {
        super(source);
        this.userId = userId;
        this.message = message;
    }
}
