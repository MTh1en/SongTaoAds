package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.constaint.TokenNaming;
import com.capstone.ads.dto.auth.AuthResponse;
import com.capstone.ads.dto.auth.LoginRequest;
import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UsersMapper;
import com.capstone.ads.model.Role;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.RoleRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.AccessTokenService;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.service.RefreshTokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;

    @Value("${app.jwt.refresh-token-ttl}")
    private int REFRESH_TOKEN_TTL;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        Users user = findUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        if (!user.getIsActive()) {
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
        String accessToken = accessTokenService.generateAccessToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken();
        refreshTokenService.saveRefreshToken(user.getEmail(), refreshToken);

        setRefreshTokenCookie(refreshToken, response);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(RegisterRequest request) {
        Role role = roleRepository.findById(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Tạo user mới
        Users newUser = usersMapper.register(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setIsActive(true);
        newUser.setRole(role);

        usersRepository.save(newUser);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = refreshTokenService.getEmail(refreshToken);
        if (!refreshTokenService.isValid(email, refreshToken)) {
            refreshTokenService.revokeToken(email);
            clearCookies(response);
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Users user = findUserByEmail(email);
        String newAccessToken = accessTokenService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Override
    public void logout(String refreshToken, HttpServletResponse response) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        var email = refreshTokenService.getEmail(refreshToken);
        refreshTokenService.revokeToken(email);
        clearCookies(response);
    }

    private Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(TokenNaming.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true) // Bật Secure để hoạt động với SameSite=None
                .path("/")
                .sameSite("None") // Cho phép cross-origin
                .maxAge(REFRESH_TOKEN_TTL)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from(TokenNaming.REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true) // Đảm bảo Secure=true khi xóa
                .path("/")
                .sameSite("None") // Phải khớp với cookie đã tạo
                .maxAge(0) // Xóa cookie
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
