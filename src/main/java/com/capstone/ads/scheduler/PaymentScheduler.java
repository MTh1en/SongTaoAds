package com.capstone.ads.scheduler;

import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.repository.internal.PaymentsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.payos.PayOS;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentScheduler {
    @NonFinal
    @Value("${payos.client-id}")
    private String CLIENT_ID;

    @NonFinal
    @Value("${payos.api-key}")
    private String API_KEY;

    @NonFinal
    @Value("${payos.checksum-key}")
    private String CHECKSUM_KEY;

    PaymentsRepository paymentsRepository;

    @Scheduled(fixedRate = 1800000000) //30 phút
    public void updateExpiredPayments() {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        List<Payments> pendingPayments = paymentsRepository.findByStatus(PaymentStatus.PENDING);
        for (Payments payment : pendingPayments) {
            try {
                var paymentLinkInfo = payOS.getPaymentLinkInformation(payment.getCode());
                if ("EXPIRED".equals(paymentLinkInfo.getStatus())) {
                    payment.setStatus(PaymentStatus.FAILED);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        paymentsRepository.saveAll(pendingPayments);
    }
}
