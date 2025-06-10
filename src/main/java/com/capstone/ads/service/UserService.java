package com.capstone.ads.service;

import com.capstone.ads.dto.user.ChangePasswordRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserCreateRequest request);

    UserDTO getUserById(String userId);

    Page<UserDTO> getUsersByRoleName(String roleName, int page, int size);

    Page<UserDTO> getAllUsers(int page, int size);

    UserDTO updateUserProfile(String userId, UserProfileUpdateRequest request);

    void deleteUser(String userId);

    UserDTO getCurrentUserProfile();

    UserDTO uploadUserAvatar(String userId, MultipartFile file) throws IOException;

    UserDTO changePassword(String userId, ChangePasswordRequest request);
}
