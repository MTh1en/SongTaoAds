package com.capstone.ads.service.impl;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.QuestionMapper;
import com.capstone.ads.model.Question;
import com.capstone.ads.model.Topic;
import com.capstone.ads.repository.internal.QuestionRepository;
import com.capstone.ads.repository.internal.TopicRepository;
import com.capstone.ads.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionDTO createQuestion(String topicId, QuestionRequest questionRequest) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
        if(questionRequest.getNumber()<1 || questionRequest.getNumber() > topic.getMaxQuestion()){
            throw new AppException(ErrorCode.NUMBER_INCORRECT);
        }
        Question question = questionMapper.createQuestion(questionRequest);
        question.setTopic(topic);
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toDto(savedQuestion);
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public List<QuestionDTO> getQuestionsByTopicId(String topicId) {
        return questionRepository.findByTopic_Id(topicId).stream()
                .sorted(Comparator.comparing(Question::getNumber))
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO updateQuestion(String id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        existingQuestion.setQuestion(questionDTO.getQuestion());
        existingQuestion.setNumber(questionDTO.getNumber());
        Question updatedQuestion = questionRepository.save(existingQuestion);
        return questionMapper.toDto(updatedQuestion);
    }

    @Override
    public void deleteQuestion(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        questionRepository.delete(question);
    }
}
