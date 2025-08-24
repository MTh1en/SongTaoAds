package com.capstone.ads.service.impl;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionCreateRequest;
import com.capstone.ads.dto.question.QuestionUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.QuestionMapper;
import com.capstone.ads.model.Question;
import com.capstone.ads.model.Topic;
import com.capstone.ads.repository.internal.QuestionRepository;
import com.capstone.ads.service.QuestionService;
import com.capstone.ads.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    TopicService topicService;

    @Override
    @Transactional
    public QuestionDTO createQuestion(String topicId, QuestionCreateRequest questionCreateRequest) {
        Topic topic = topicService.getTopicById(topicId);

        Question question = questionMapper.mapCreateRequestToEntity(questionCreateRequest);
        question.setTopic(topic);
        question = questionRepository.save(question);

        return questionMapper.toDto(question);
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO findQuestionById(String questionId) {
        Question question = getQuestionById(questionId);
        return questionMapper.toDto(question);
    }

    @Override
    public List<QuestionDTO> findQuestionsByTopicId(String topicId) {
        return questionRepository.findByTopic_Id(topicId).stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(String questionId, QuestionUpdateRequest request) {
        Question question = getQuestionById(questionId);

        questionMapper.mapUpdateRequestToEntity(request, question);
        question = questionRepository.save(question);

        return questionMapper.toDto(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(String questionId) {
        Question question = getQuestionById(questionId);
        questionRepository.delete(question);
    }


    //INTERNAL FUNCTION
    public Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
    }
}
