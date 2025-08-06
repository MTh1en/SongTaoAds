package com.capstone.ads.service.impl;

import com.capstone.ads.dto.topic.TopicCreateRequest;
import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicUpdateInformationRequest;
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
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    @Transactional
    public TopicDTO createTopic(TopicCreateRequest topicCreateRequest) {
        Topic topic = topicMapper.mapCreateRequestToEntity(topicCreateRequest);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }


    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO findTopicById(String id) {
        Topic topic = getTopicById(id);
        return topicMapper.toDto(topic);
    }


    @Override
    @Transactional
    public TopicDTO updateTopic(String topicId, TopicUpdateInformationRequest request) {
        Topic existingTopic = getTopicById(topicId);

        topicMapper.mapUpdateRequestToEntity(request, existingTopic);

        Topic updatedTopic = topicRepository.save(existingTopic);
        return topicMapper.toDto(updatedTopic);
    }

    @Override
    @Transactional
    public void deleteTopic(String topicId) {
        Topic topic = getTopicById(topicId);
        topicRepository.delete(topic);
    }

    //INTERNAL FUNCTION

    public Topic getTopicById(String topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
    }
}
