package com.capstone.ads.service.impl;

import com.capstone.ads.dto.user.ChangePasswordRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UsersMapper;
import com.capstone.ads.model.Role;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.repository.internal.RoleRepository;

import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UsersMapper usersMapper;
    private final SecurityContextUtils securityContextUtils;
    private final S3Repository s3Repository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
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
    public UserDTO updateUserProfile(String userId, UserProfileUpdateRequest request) {
        Users user = findUserByIdAndActive(userId);

        // Use MapStruct to update entity fields
        usersMapper.updateUserUpdateRequestToEntity(request, user);

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
    public UserDTO getCurrentUserProfile() {
        Users user = securityContextUtils.getCurrentUser();
        return convertUserToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO uploadUserAvatar(String userId, MultipartFile file) throws IOException {
        Users user = findUserByIdAndActive(userId);
        String avatarName = generateAvatarName(user.getId());
        s3Repository.uploadSingleFile(bucketName, file.getBytes(), avatarName, file.getContentType());
        user.setAvatar(avatarName);
        usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    public UserDTO changePassword(String userId, ChangePasswordRequest request) {
        Users user = findUserByIdAndActive(userId);
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    private String generateAvatarName(String userId) {
        return String.format("avatar/%s/%s", userId, UUID.randomUUID());
    }

    private UserDTO convertUserToDTO(Users user) {
        var userResponse = usersMapper.toDTO(user);
        if (!Objects.isNull(userResponse.getAvatar())) {
            var avatarImageDownloadFromS3 = s3Repository.generatePresignedUrl(bucketName, user.getAvatar(), 30);
            userResponse.setAvatar(avatarImageDownloadFromS3);
        }
        return userResponse;
    }

    private Users findUserByIdAndActive(String userId) {
        return usersRepository.findByIdAndIsActive(userId, true)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}