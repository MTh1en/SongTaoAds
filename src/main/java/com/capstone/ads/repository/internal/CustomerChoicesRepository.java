package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    List<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);
}