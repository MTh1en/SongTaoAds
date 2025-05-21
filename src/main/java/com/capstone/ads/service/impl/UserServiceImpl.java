package com.capstone.ads.service.impl;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UsersMapper;
import com.capstone.ads.model.Role;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.repository.internal.RoleRepository;

import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UsersMapper usersMapper;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final SecurityContextUtils securityContextUtils;

    @Override
    public UserDTO createUser(UserRequest request) {
        // Validate uniqueness
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (usersRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Validate role
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Map DTO to entity
        Users user = usersMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        user.setRole(role);

        user = usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserById(String id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return usersMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(usersMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public UserDTO updateUser(String id, UserRequest request) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Validate uniqueness if email or phone is changed
        if (!user.getEmail().equals(request.getEmail()) && usersRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (!user.getPhone().equals(request.getPhone()) && usersRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Validate role
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Use MapStruct to update entity fields
        usersMapper.updateEntityFromDTO(request, user);

        // Manually handle password and role
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRole(role);

        Users updatedUser = usersRepository.save(user);
        return usersMapper.toDTO(updatedUser);
    }
    @Override
    public void deleteUser(String id) {
        if (!usersRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        usersRepository.deleteById(id);
    }
    @Override
    public UserDTO getProfile() {
        Users user = securityContextUtils.getCurrentUser();
        return usersMapper.toDTO(user);
}
}