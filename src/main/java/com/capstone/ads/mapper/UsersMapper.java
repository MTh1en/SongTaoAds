package com.capstone.ads.mapper;

import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users register(RegisterRequest registerRequest);
}
