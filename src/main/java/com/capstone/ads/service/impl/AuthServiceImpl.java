package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.constaint.TokenNaming;
import com.capstone.ads.dto.auth.AuthResponse;
import com.capstone.ads.dto.auth.ExchangeTokenRequest;
import com.capstone.ads.dto.auth.LoginRequest;
import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.UsersMapper;
import com.capstone.ads.model.Roles;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.external.OutboundIdentityClient;
import com.capstone.ads.repository.external.OutboundUserClient;
import com.capstone.ads.repository.internal.RolesRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.AccessTokenService;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.service.RefreshTokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UsersRepository usersRepository;
    UsersMapper usersMapper;
    AccessTokenService accessTokenService;
    RefreshTokenService refreshTokenService;
    RolesRepository rolesRepository;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;

    @NonFinal
    @Value("${app.jwt.refresh-token-ttl}")
    private int REFRESH_TOKEN_TTL;

    @NonFinal
    @Value("${app.outbound.identity.client-id}")
    private String CLIENT_ID;

    @NonFinal
    @Value("${app.outbound.identity.client-secret}")
    private String CLIENT_SECRET;

    @NonFinal
    @Value("${app.outbound.identity.redirect-uri}")
    private String REDIRECT_URI;

    @NonFinal
    private final String GRANT_TYPE = "authorization_code";

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public AuthResponse outboundAuthenticate(String code, HttpServletResponse response) {
        var exchangeToken = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        var userInfo = outboundUserClient.getUserInfo("json", exchangeToken.getAccessToken());
        Roles roles = rolesRepository.findById(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        var user = usersRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> usersRepository.save(Users.builder()
                        .fullName(userInfo.getName())
                        .email(userInfo.getEmail())
                        .avatar(userInfo.getPicture())
                        .roles(roles)
                        .isActive(true)
                        .build()));

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
        Roles roles = rolesRepository.findById(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Tạo user mới
        Users newUser = usersMapper.register(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setIsActive(false);
        newUser.setRoles(roles);

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

    @Override
    public void verifyUser(String email) {
        Users verificationUser = findUserByEmail(email);
        verificationUser.setIsActive(true);
        usersRepository.save(verificationUser);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        Users resetPasswordUser = findUserByEmail(email);
        resetPasswordUser.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(resetPasswordUser);
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
