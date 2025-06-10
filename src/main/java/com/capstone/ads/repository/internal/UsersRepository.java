package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByIdAndIsActive(String id, Boolean isActive);

    Optional<Users> findByIdAndIsActiveAndRoles_Name(String id, Boolean isActive, String name);

    Page<Users> findByIsActiveAndRoles_Name(Boolean isActive, String name, Pageable pageable);
}