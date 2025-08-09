package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ModelChatBot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelChatBotRepository extends JpaRepository<ModelChatBot, String> {
    ModelChatBot findByPreviousModelName(String previousModelName);

    ModelChatBot findByModelName(String modelName);


    Optional<ModelChatBot> findByActiveTrue();

    Optional<ModelChatBot> getModelChatBotByActive(boolean active);
}