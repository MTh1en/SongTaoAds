package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    Optional<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);
}