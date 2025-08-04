package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Page<Conversation> findByTopic_Id(String id, Pageable pageable);

    Page<Conversation> findByUser_Id(String id, Pageable pageable);

    Page<Conversation> findByModelChatBot_Id(String id, Pageable pageable);

}
