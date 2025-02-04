package com.mitchmele.feedback_listener.util;

import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AppLogger {
    private AppLogger() {} //singleton

    public static void logResult(ProcessingResult processingResult, String message) {
        if (processingResult.getProcessingStatus().equals(ProcessingStatus.SUCCESS)) {
            log.info("successfully saved feedback to Db: {}", message);
        } else {{
            log.error("failed to save feedback to Db: {}", message);
        }}
    }

    public static void logSuccess(String id) {
        log.info("feedback saved for id: {}", id);
    }
    public static void logFailure(String message) {
        log.info("error while saving feedback: {}", message);
    }
}
