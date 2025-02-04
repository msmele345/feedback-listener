package com.mitchmele.feedback_listener.controller;

import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.service.FeedbackRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/retry")
@RequiredArgsConstructor
public class RetryErrorController {

    private final FeedbackRetryService feedbackRetryService;

    @PostMapping("/feedback")
    public ResponseEntity<ProcessingResult> retryProcessing(@RequestBody ErrorEventPayload payload) {
        return feedbackRetryService.processAndSaveFeedback(payload);
    }
}
