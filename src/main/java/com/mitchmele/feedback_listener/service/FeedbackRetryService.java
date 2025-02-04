package com.mitchmele.feedback_listener.service;

import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.ProcessingRetryException;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackRetryService {

    private final DbService dbService;

    public ResponseEntity<ProcessingResult> processAndSaveFeedback(ErrorEventPayload payload) {
        final ProcessingResult result = dbService.saveRetryMessage(payload);

        if(result.getProcessingStatus().equals(ProcessingStatus.FAILURE)) {
            throw new ProcessingRetryException("Failed to save feedback");
            //throw exception for controller advice
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
