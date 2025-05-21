package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextUtils {
    private final UsersRepository usersRepository;

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. Validate authentication
        if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.warn("Unauthenticated access attempt or anonymous user");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // 2. Get email from principal
        String email = authentication.getName();
        if (ObjectUtils.isEmpty(email)) {
            log.error("Invalid principal type: {}", authentication.getPrincipal().getClass());
            throw new AppException(ErrorCode.INVALID_PRINCIPAL);
        }

        // 3. Query database
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found in DB: {}", email);
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }
}