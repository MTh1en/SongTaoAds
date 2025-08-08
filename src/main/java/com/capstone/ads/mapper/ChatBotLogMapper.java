package com.capstone.ads.mapper;

import com.capstone.ads.dto.chatBot.ChatBotDTO;
import com.capstone.ads.model.ChatBotLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatBotLogMapper {
    ChatBotDTO toDTO(ChatBotLog chatBotLog);
}
