package com.capstone.ads.service;

import com.capstone.ads.dto.email.TransactionalEmailResponse;

public interface RecoveryService {
    TransactionalEmailResponse sendVerifyEmail(String email);

    TransactionalEmailResponse sendResetPasswordEmail(String email);

    Boolean validateVerificationCode(String email, String verificationCode);

    Boolean validateResetCode(String email, String resetCode, Boolean isDeleted);
}
