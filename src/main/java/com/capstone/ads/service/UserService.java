package com.capstone.ads.service;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequest;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserRequest request);
    UserDTO getUserById(String id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(String id, UserRequest request);
    void deleteUser(String id);
    UserDTO getProfile();
}
