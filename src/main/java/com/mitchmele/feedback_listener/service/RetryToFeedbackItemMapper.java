package com.mitchmele.feedback_listener.service;

import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import org.springframework.stereotype.Component;

@Component
public class RetryToFeedbackItemMapper implements EventMapper<ErrorEventPayload, FeedbackItem>{

    @Override
    public FeedbackItem mapFrom(ErrorEventPayload event) {
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setFeedbackId(event.getFeedbackId());
        feedbackItem.setEmail(event.getEmail());
        feedbackItem.setContent(event.getEmail());
        feedbackItem.setSubmissionTime(event.getSubmissionTime());
        feedbackItem.setErrorRetry(true);
        return feedbackItem;
    }
}
