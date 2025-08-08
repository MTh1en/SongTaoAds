package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.auth.ResetPasswordRequest;
import com.capstone.ads.dto.email.ResendVerificationEmailRequest;
import com.capstone.ads.dto.email.SendResetPasswordEmailRequest;
import com.capstone.ads.dto.email.TransactionalEmailResponse;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.service.RecoveryService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "RECOVERY")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecoveryController {
    RecoveryService recoveryService;
    AuthService authService;

    @PostMapping("/verifications/resend")
    @Operation(summary = "Gửi lại email xác nhận tài khoản")
    public ApiResponse<TransactionalEmailResponse> resendVerificationEmail(@Valid @RequestBody ResendVerificationEmailRequest request) {
        var response = recoveryService.sendVerifyEmail(request.getEmail());
        return ApiResponseBuilder.buildSuccessResponse("Gửi lại email xác nhận tài khoản thành công", response);
    }

    @GetMapping("/verifications/verify")
    public ModelAndView verifyAccount(@RequestParam String email, @RequestParam String code) {
        if (recoveryService.validateVerificationCode(email, code)) {
            authService.verifyUser(email);
            return new ModelAndView("VerificationSuccess");
        } else {
            return new ModelAndView("VerificationFailure");
        }
    }

    // ==== FORGOT PASSWORD ====== //

    @PostMapping("/password-reset/resend")
    @Operation(summary = "Gửi email đặt lại mật khẩu")
    public ApiResponse<TransactionalEmailResponse> sendResetPasswordEmail(@Valid @RequestBody SendResetPasswordEmailRequest request) {
        var response = recoveryService.sendResetPasswordEmail(request.getEmail());
        return ApiResponseBuilder.buildSuccessResponse("Gửi lại email đặt lại mật khẩu thành công", response);
    }

    @GetMapping("/password-reset/form")
    public ModelAndView showResetForm(@RequestParam String email, @RequestParam String code) {
        if (recoveryService.validateResetCode(email, code, false)) {
            ModelAndView modelAndView = new ModelAndView("ResetPassword");
            ResetPasswordRequest resetForm = new ResetPasswordRequest();
            resetForm.setEmail(email);
            resetForm.setCode(code);
            modelAndView.addObject("resetForm", resetForm);
            return modelAndView;
        }
        return new ModelAndView("PasswordResetExpired");
    }

    @PostMapping("/password-reset/reset")
    public ModelAndView processReset(@Valid @ModelAttribute("resetForm") ResetPasswordRequest resetForm,
                                     BindingResult result) {
        String email = resetForm.getEmail();
        String code = resetForm.getCode();
        String newPassword = resetForm.getNewPassword();
        String confirmPassword = resetForm.getConfirmPassword();
        if (result.hasErrors()) {
            return new ModelAndView("ResetPassword");
        }
        if (recoveryService.validateResetCode(email, code, true)) {
            if (newPassword != null && newPassword.equals(confirmPassword)) {
                authService.resetPassword(email, newPassword);
                return new ModelAndView("PasswordResetSuccess");
            } else {
                ModelAndView modelAndView = new ModelAndView("ResetPassword");
                modelAndView.addObject("resetForm", resetForm);
                modelAndView.addObject("code", code);
                modelAndView.addObject("email", email);
                modelAndView.addObject("error", "Mật khẩu không khớp!");
                return modelAndView;
            }
        } else {
            return new ModelAndView("PasswordResetExpired");
        }
    }
}

