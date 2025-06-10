package com.capstone.ads.service;

import com.capstone.ads.dto.email.TransactionalEmailResponse;

public interface VerificationService {
    TransactionalEmailResponse sendVerifyEmail(String fullName, String email);

    String generateVerificationCode(String email);

    Boolean validateVerificationCode(String email, String verificationCode);
}
