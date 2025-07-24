package com.capstone.ads.repository.internal;

import com.capstone.ads.dto.chatBot.FrequentQuestion;
import com.capstone.ads.model.ChatBotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotLogRepository extends JpaRepository<ChatBotLog, String> {
    @Query("SELECT new com.capstone.ads.dto.chatBot.FrequentQuestion(cl.question, COUNT(*) as frequency) " +
            "FROM ChatBotLog cl " +
            "GROUP BY cl.question " +
            "ORDER BY frequency DESC")
    List<FrequentQuestion> findTop10FrequentQuestions();
}
