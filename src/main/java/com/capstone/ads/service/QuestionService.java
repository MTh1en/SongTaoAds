package com.capstone.ads.service;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionRequest;

import java.util.List;

public interface QuestionService {
    QuestionDTO createQuestion(String topicId, QuestionRequest questionRequest);

    List<QuestionDTO> getAllQuestions();

    QuestionDTO getQuestionById(String id);

    List<QuestionDTO> getQuestionsByTopicId(String topicId);

    QuestionDTO updateQuestion(String id, QuestionDTO questionDTO);

    void deleteQuestion(String id);
}
