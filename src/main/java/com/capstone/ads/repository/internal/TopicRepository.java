package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, String> {
}
