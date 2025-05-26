package com.capstone.ads.service;

import com.capstone.ads.dto.user.ChangePasswordRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserCreateRequest request);

    UserDTO getUserById(String userId);

    List<UserDTO> getAllUsers();

    UserDTO updateUserProfile(String userId, UserProfileUpdateRequest request);

    void deleteUser(String userId);

    UserDTO getCurrentUserProfile();

    UserDTO uploadUserAvatar(String userId, MultipartFile file) throws IOException;

    UserDTO changePassword(String userId, ChangePasswordRequest request);
}
