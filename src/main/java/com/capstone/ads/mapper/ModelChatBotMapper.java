package com.capstone.ads.mapper;

import com.capstone.ads.dto.model_chat.ModelChatBotDTO;
import com.capstone.ads.model.ModelChatBot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelChatBotMapper {
    ModelChatBot toEntity(ModelChatBotDTO dto);

    ModelChatBotDTO toDTO(ModelChatBot entity);
}
