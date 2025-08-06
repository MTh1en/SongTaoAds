package com.capstone.ads.service;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionCreateRequest;
import com.capstone.ads.dto.question.QuestionUpdateRequest;

import java.util.List;

public interface QuestionService {
    QuestionDTO createQuestion(String topicId, QuestionCreateRequest questionCreateRequest);

    List<QuestionDTO> getAllQuestions();

    QuestionDTO findQuestionById(String id);

    List<QuestionDTO> getQuestionsByTopicId(String topicId);

    QuestionDTO updateQuestion(String id, QuestionUpdateRequest request);

    void deleteQuestion(String id);
}
