package com.capstone.ads.service;

public interface CalculateService {
    Long calculateSubtotal(String customerChoicesDetailId);

    Long calculateTotal(String customerChoiceId);
}
