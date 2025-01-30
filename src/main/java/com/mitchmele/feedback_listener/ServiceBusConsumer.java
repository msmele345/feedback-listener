package com.mitchmele.feedback_listener;

import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import com.mitchmele.feedback_listener.service.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.mitchmele.feedback_listener.service.AppLogger.logResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceBusConsumer { //this is a backend service for the screens UI.

    private final DbService dbService;

    @ServiceBusListener(destination = "bustopic2932", group = "feedback")
    public void processMessages(String message) {
        log.info("inbound consumer message. Processing: {}", message);

        final ProcessingResult processingResult = dbService.saveMessage(message);
        //add error service?
        logResult(processingResult, message);
    }

}
