package com.mitchmele.feedback_listener;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.mitchmele.feedback_listener.model.FeedbackItem;

public interface FeedbackRepository extends CosmosRepository<FeedbackItem, String> {
}
