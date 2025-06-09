package com.capstone.ads.service.impl;

import com.capstone.ads.dto.email.TransactionalEmailRequest;
import com.capstone.ads.dto.email.TransactionalEmailResponse;
import com.capstone.ads.dto.email.transactional.Params;
import com.capstone.ads.dto.email.transactional.Recipient;
import com.capstone.ads.dto.email.transactional.Sender;
import com.capstone.ads.mapper.VerificationMapper;
import com.capstone.ads.repository.external.BrevoRepository;
import com.capstone.ads.service.VerificationService;
import lombok.RequiredArgsConstructor;
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
public class VerificationServiceImpl implements VerificationService {
    @Value("${brevo.key}")
    private String brevoKey;
    @Value("${brevo.sender-email}")
    private String senderEmail;
    @Value("${brevo.sender-name}")
    private String senderName;
    @Value("${brevo.verify-url}")
    private String verifyUrl;


    private final BrevoRepository brevoRepository;
    private final VerificationMapper verificationMapper;
    private final SpringTemplateEngine templateEngine;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public TransactionalEmailResponse sendVerifyEmail(String fullName, String email) {
        Sender sender = verificationMapper.toSender(senderName, senderEmail);

        List<Recipient> to = new ArrayList<>(List.of(new Recipient(fullName, email)));

        Params params = verificationMapper.toParams(fullName, verifyUrl);

        String subject = "Xác minh tài khoản";
        String verificationCode = generateVerificationCode(email);

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("url", String.format(verifyUrl, email, verificationCode));
        String htmlContent = templateEngine.process("VerificationEmail", context);

        TransactionalEmailRequest emailRequest = verificationMapper.toTransactionalEmailRequest(sender, to, params, subject, htmlContent);
        return brevoRepository.sendTransactionalEmail(brevoKey, emailRequest);
    }

    @Override
    public String generateVerificationCode(String email) {
        String verificationCode = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(verificationCode, email, 15, TimeUnit.MINUTES);
        return verificationCode;
    }

    @Override
    public Boolean validateVerificationCode(String email, String verificationCode) {
        String emailStored = redisTemplate.opsForValue().get(verificationCode);
        if (emailStored == null) {
            return false;
        }
        boolean isValid = email.equals(emailStored);
        if (isValid) redisTemplate.delete(verificationCode);
        return isValid;
    }
}
