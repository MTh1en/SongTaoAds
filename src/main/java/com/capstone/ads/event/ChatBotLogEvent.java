package com.capstone.ads.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotLogEvent extends ApplicationEvent {
    String response;
    String question;
    String modelChatBotId;

    public ChatBotLogEvent(Object source, String response, String question, String modelChatBotId) {
        super(source);
        this.response = response;
        this.question = question;
        this.modelChatBotId = modelChatBotId;
    }
}
