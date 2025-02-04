package com.mitchmele.feedback_listener.model;

public class ProcessingRetryException extends RuntimeException {
    public ProcessingRetryException(String message) {
        super(message);
    }
}
