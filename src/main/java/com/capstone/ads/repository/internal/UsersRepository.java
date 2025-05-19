package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}