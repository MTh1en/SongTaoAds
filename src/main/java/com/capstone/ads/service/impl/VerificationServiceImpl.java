package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.RedisKeyNaming;
import com.capstone.ads.dto.email.TransactionalEmailRequest;
import com.capstone.ads.dto.email.TransactionalEmailResponse;
import com.capstone.ads.dto.email.transactional.Recipient;
import com.capstone.ads.dto.email.transactional.Sender;
import com.capstone.ads.mapper.VerificationMapper;
import com.capstone.ads.repository.external.BrevoClient;
import com.capstone.ads.service.VerificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationServiceImpl implements VerificationService {
    @NonFinal
    @Value("${brevo.key}")
    private String brevoKey;

    @NonFinal
    @Value("${brevo.sender-email}")
    private String senderEmail;

    @NonFinal
    @Value("${brevo.sender-name}")
    private String senderName;

    @NonFinal
    @Value("${brevo.verify-url}")
    private String verifyUrl;

    @NonFinal
    @Value("${brevo.password-reset-url}")
    private String passwordResetUrl;

    BrevoClient brevoClient;
    VerificationMapper verificationMapper;
    SpringTemplateEngine templateEngine;
    RedisTemplate<String, String> redisTemplate;

    @Override
    public TransactionalEmailResponse sendVerifyEmail(String email) {
        Sender sender = verificationMapper.toSender(senderName, senderEmail);

        List<Recipient> to = new ArrayList<>(List.of(
                verificationMapper.toRecipient(email))
        );

        String subject = "Xác minh tài khoản";
        String verificationCode = generateVerificationCode(email);

        Context context = new Context();
        context.setVariable("url", String.format(verifyUrl, email, verificationCode));
        String htmlContent = templateEngine.process("VerificationEmail", context);

        TransactionalEmailRequest emailRequest = verificationMapper.toTransactionalEmailRequest(sender, to, subject, htmlContent);
        return brevoClient.sendTransactionalEmail(brevoKey, emailRequest);
    }

    @Override
    public TransactionalEmailResponse sendResetPasswordEmail(String email) {
        Sender sender = verificationMapper.toSender(senderName, senderEmail);

        List<Recipient> to = new ArrayList<>(List.of(
                verificationMapper.toRecipient(email))
        );

        String subject = "Đặt lại mật khẩu";
        String resetCode = generateResetCode(email);
        String resetLink = String.format(passwordResetUrl, email, resetCode);

        Context context = new Context();
        context.setVariable("url", resetLink);
        String htmlContent = templateEngine.process("PasswordResetEmail", context);

        TransactionalEmailRequest emailRequest = verificationMapper.toTransactionalEmailRequest(sender, to, subject, htmlContent);
        return brevoClient.sendTransactionalEmail(brevoKey, emailRequest);
    }

    @Override
    public Boolean validateVerificationCode(String email, String verificationCode) {
        String verificationEmailKey = RedisKeyNaming.VERIFICATION_EMAIL + email;

        String storedCode = redisTemplate.opsForValue().get(verificationEmailKey);
        if (storedCode == null) {
            return false;
        }
        boolean isValid = verificationCode.equals(storedCode);
        if (isValid) redisTemplate.delete(verificationEmailKey);
        return isValid;
    }

    @Override
    public Boolean validateResetCode(String email, String resetCode, Boolean isDeleted) {
        String resetEmailKey = RedisKeyNaming.PASSWORD_RESET_EMAIL + email;

        String storedCode = redisTemplate.opsForValue().get(resetEmailKey);
        if (storedCode == null) {
            return false;
        }
        boolean isValid = resetCode.equals(storedCode);
        if (isValid && isDeleted) redisTemplate.delete(resetEmailKey);
        return isValid;
    }

    public String generateVerificationCode(String email) {
        String verificationCode = UUID.randomUUID().toString();
        if (redisTemplate.hasKey(email)) {
            redisTemplate.delete(email);
        }

        String verificationEmailKey = RedisKeyNaming.VERIFICATION_EMAIL + email;
        redisTemplate.opsForValue().set(verificationEmailKey, verificationCode, 15, TimeUnit.MINUTES);
        return verificationCode;
    }

    public String generateResetCode(String email) {
        String resetCode = UUID.randomUUID().toString();
        if (redisTemplate.hasKey(email)) {
            redisTemplate.delete(email);
        }
        String resetKey = RedisKeyNaming.PASSWORD_RESET_EMAIL + email;
        redisTemplate.opsForValue().set(resetKey, resetCode, 15, TimeUnit.MINUTES);
        return resetCode;
    }
}
