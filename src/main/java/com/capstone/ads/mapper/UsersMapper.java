package com.capstone.ads.mapper;

import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users register(RegisterRequest registerRequest);

    UserDTO toDTO(Users users);

    @Mapping(target = "password", ignore = true) // Password will be handled separately
    @Mapping(target = "role", ignore = true)
    Users toEntity(UserCreateRequest request);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserUpdateRequestToEntity(UserProfileUpdateRequest request, @MappingTarget Users entity);
}
