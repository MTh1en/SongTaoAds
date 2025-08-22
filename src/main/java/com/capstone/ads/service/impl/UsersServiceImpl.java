package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.user.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UsersMapper;
import com.capstone.ads.model.Roles;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.repository.internal.RolesRepository;

import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersServiceImpl implements UserService {
    FileDataService fileDataService;
    RolesRepository rolesRepository;
    UsersRepository usersRepository;
    UsersMapper usersMapper;
    SecurityContextUtils securityContextUtils;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        Roles roles = rolesRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Users user = usersMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        user.setIsBanned(false);
        user.setRoles(roles);

        user = usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    public UserDTO banOrUnbanUser(String userId, boolean isBanned) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setIsBanned(isBanned);
        user = usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    public UserDTO findUserById(String id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return usersMapper.toDTO(user);
    }

    @Override
    public Page<UserDTO> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return usersRepository.findAll(pageable)
                .map(usersMapper::toDTO);
    }

    @Override
    public Page<UserDTO> findUsersByRoleName(String roleName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (!isValidRole(roleName)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return usersRepository.findByIsActiveAndRoles_Name(true, roleName, pageable)
                .map(usersMapper::toDTO);
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(String userId, UserProfileUpdateRequest request) {
        Users user = this.getUserByIdAndIsActiveAndNotBanned(userId);

        // Use MapStruct to updateProductTypeInformation entity fields
        usersMapper.updateUserUpdateRequestToEntity(request, user);

        Users updatedUser = usersRepository.save(user);
        return usersMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        if (!usersRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        usersRepository.deleteById(id);
    }

    @Override
    public UserDTO getCurrentUserProfile() {
        Users user = securityContextUtils.getCurrentUser();
        return usersMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO uploadUserAvatar(String userId, MultipartFile file) {
        Users user = this.getUserByIdAndIsActiveAndNotBanned(userId);
        String avatarName = generateAvatarName(user.getId());
        fileDataService.uploadSingleFile(avatarName, file);
        user.setAvatar(avatarName);
        usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO changePassword(String userId, ChangePasswordRequest request) {
        Users user = this.getUserByIdAndIsActiveAndNotBanned(userId);
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO newPasswordForOutboundAccount(String userId, NewPasswordRequest request) {
        Users user = this.getUserByIdAndIsActiveAndNotBanned(userId);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        usersRepository.save(user);
        return usersMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserExistsAndIsActive(String userId) {
        if (!usersRepository.existsByIdAndIsActive(userId, true)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public Users getUserByIdAndIsActiveAndNotBanned(String userId) {
        return usersRepository.findByIdAndIsActiveAndIsBanned(userId, true, false)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Users getUsersByIdAndIsActiveAndRoleName(String userId, boolean isActive, String roleName) {
        return usersRepository.findByIdAndIsActiveAndRoles_Name(userId, true, roleName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Users verifyAccountToSendEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (users.getIsBanned()) {
            throw new AppException(ErrorCode.ACCOUNT_BANNED);
        }
        return users;
    }

    private String generateAvatarName(String userId) {
        return String.format(S3ImageKeyFormat.AVATAR, userId);
    }

    private boolean isValidRole(String role) {
        return role.equals(PredefinedRole.ADMIN_ROLE) ||
                role.equals(PredefinedRole.CUSTOMER_ROLE) ||
                role.equals(PredefinedRole.STAFF_ROLE) ||
                role.equals(PredefinedRole.DESIGNER_ROLE) ||
                role.equals(PredefinedRole.SALE_ROLE);
    }
}