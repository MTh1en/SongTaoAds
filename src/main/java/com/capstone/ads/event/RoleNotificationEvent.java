package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleNotificationEvent extends ApplicationEvent {
    private String roleName;
    private String message;

    public RoleNotificationEvent(Object source, String roleName, String message) {
        super(source);
        this.roleName = roleName;
        this.message = message;
    }
}
