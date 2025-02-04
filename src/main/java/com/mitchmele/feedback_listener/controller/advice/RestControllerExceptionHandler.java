package com.mitchmele.feedback_listener.controller.advice;

import com.mitchmele.feedback_listener.model.ProcessingRetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = ProcessingRetryException.class)
    public ResponseEntity<ErrorResponse> handleProcessingRetryException(ProcessingRetryException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage("retry attempt failed").build();
        log.info("Processing retry attempt failed. Error message: {}", e.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
