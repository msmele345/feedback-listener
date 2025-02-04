package com.mitchmele.feedback_listener.controller.advice;

import com.mitchmele.feedback_listener.model.ProcessingRetryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = ProcessingRetryException.class)
    public ResponseEntity<ErrorResponse> handleProcessingRetryException(ProcessingRetryException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage("retry attempt failed").build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
