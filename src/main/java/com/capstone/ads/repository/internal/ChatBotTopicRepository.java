package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ChatBotTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatBotTopicRepository extends JpaRepository<ChatBotTopic, String> {
    List<ChatBotTopic> findByModelChatBot_Id(String id);

    List<ChatBotTopic> findByTopic_Id(String id);

    boolean existsByModelChatBot_IdAndTopic_Id(String id, String id1);
}
