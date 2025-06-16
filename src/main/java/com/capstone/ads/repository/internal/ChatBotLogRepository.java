package com.capstone.ads.repository.internal;

import com.capstone.ads.model.ChatBotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotLogRepository extends JpaRepository<ChatBotLog, String> {
}
