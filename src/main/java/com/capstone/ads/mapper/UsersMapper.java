package com.capstone.ads.mapper;

import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequestDTO;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users register(RegisterRequest registerRequest);
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    @Mapping(source = "role.name", target = "roleName")
    UserDTO toDTO(Users users);

    @Mapping(target = "password", ignore = true) // Password will be handled separately
    @Mapping(target = "role", ignore = true) // Role will be set manually in service
    Users toEntity(UserRequestDTO request);
}
