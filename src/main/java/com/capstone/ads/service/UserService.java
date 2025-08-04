package com.capstone.ads.service;

import com.capstone.ads.dto.user.ChangePasswordRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import com.capstone.ads.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDTO createUser(UserCreateRequest request);

    UserDTO findUserById(String userId);

    Page<UserDTO> findUsersByRoleName(String roleName, int page, int size);

    Page<UserDTO> findAllUsers(int page, int size);

    UserDTO updateUserProfile(String userId, UserProfileUpdateRequest request);

    void deleteUser(String userId);

    UserDTO getCurrentUserProfile();

    UserDTO uploadUserAvatar(String userId, MultipartFile file) throws IOException;

    UserDTO changePassword(String userId, ChangePasswordRequest request);

    //INTERNAL FUNCTION
    void validateUserExistsAndIsActive(String userId);

    Users getUserByIdAndIsActive(String userId);

    Users getUsersByIdAndIsActiveAndRoleName(String userId, boolean isActive, String roleName);

    Users getUserByEmail(String email);
}
