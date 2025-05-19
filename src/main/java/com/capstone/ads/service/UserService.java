package com.capstone.ads.service;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequestDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserRequestDTO request);
    UserDTO getUserById(String id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(String id, UserRequestDTO request);
    void deleteUser(String id);
    UserDTO getProfile();
}
