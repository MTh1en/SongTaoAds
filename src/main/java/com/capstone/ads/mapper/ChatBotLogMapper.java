package com.capstone.ads.mapper;

import com.capstone.ads.dto.chatBot.ChatBotDTO;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatBotLogMapper {
    ChatBotDTO toDTO(ChatBotLog chatBotLog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", expression = "java(mapUsers(userId))")
    ChatBotLog toEntity(String question, String answer, String userId);

    default Users mapUsers(String userId) {
        if (userId == null) return null;
        Users users = new Users();
        users.setId(userId);
        return users;
    }
}
