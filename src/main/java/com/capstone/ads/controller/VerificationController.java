package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.email.ResendVerificationEmailRequest;
import com.capstone.ads.dto.email.TransactionalEmailResponse;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.service.VerificationService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {
    private final VerificationService verificationService;
    private final AuthService authService;

    @PostMapping("/verifications/resend")
    public ApiResponse<TransactionalEmailResponse> resendVerificationEmail(@Valid @RequestBody ResendVerificationEmailRequest request) {
        var response = verificationService.sendVerifyEmail(request.getFullName(), request.getEmail());
        return ApiResponseBuilder.buildSuccessResponse("Resend email verified successfully", response);
    }

    @GetMapping("/verification/verify")
    public ModelAndView verifyAccount(@RequestParam String email, @RequestParam String code) {
        if (verificationService.validateVerificationCode(email, code)) {
            authService.verifyUser(email);
            return new ModelAndView("VerificationSuccess");
        } else {
            return new ModelAndView("VerificationFailure");
        }
    }
}
