package com.capstone.ads.service.impl;

import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.TopicMapper;
import com.capstone.ads.model.Topic;
import com.capstone.ads.repository.internal.TopicRepository;
import com.capstone.ads.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl  implements TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Transactional
    @Override
    public TopicDTO createTopic(TopicRequest topicRequest) {
        Topic topic = topicMapper.createTopic(topicRequest);
        Topic savedTopic = topicRepository.save(topic);
        return topicMapper.toDto(savedTopic);
    }

    @Transactional
    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO getTopicById(String id) {
        return topicRepository.findById(id)
                .map(topicMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
    }

    @Transactional
    @Override
    public TopicDTO updateTopic(String id, TopicDTO topicDTO) {
        Topic existingTopic = topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
        existingTopic.setTitle(topicDTO.getTitle());
        existingTopic.setDescription(topicDTO.getDescription());
        Topic updatedTopic = topicRepository.save(existingTopic);
        return topicMapper.toDto(updatedTopic);
    }

    @Override
    public void deleteTopic(String id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
        topicRepository.delete(topic);
    }
}
