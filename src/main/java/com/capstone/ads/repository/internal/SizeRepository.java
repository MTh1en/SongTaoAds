package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Sizes, String> {
}