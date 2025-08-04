package com.capstone.ads.repository.internal;

import com.capstone.ads.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse, String> {
    List<UserResponse> findByQuestion_Id(String id);

    List<UserResponse> findByConversation_Id(String id);

}
