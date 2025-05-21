package com.capstone.ads.service;

public interface CalculateService {
    Double calculateSubtotal(String customerChoicesDetailId);

    Double calculateTotal(String customerChoiceId);
}
