package com.capstone.ads.mapper;
import com.capstone.ads.dto.user_response.UserResponseDTO;
import com.capstone.ads.dto.user_response.UserResponseRequest;
import com.capstone.ads.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "conversationId", source = "conversation.id")
    UserResponseDTO toDto(UserResponse userResponse);

    UserResponse toEntity(UserResponseRequest userResponseRequest);
}
