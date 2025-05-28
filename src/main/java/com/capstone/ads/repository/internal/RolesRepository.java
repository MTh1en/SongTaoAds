package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, String> {
    Optional<Roles> findByName(String name);

}