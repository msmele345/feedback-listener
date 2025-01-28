package com.mitchmele.feedback_listener;

import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusMessageListener { //this is a backend service for the screens UI.

    @ServiceBusListener(destination = "bustopic2932", group = "feedback")
    public void processMessages(String message) {
        log.info("CONSUMER MESSAGE: {}", message);
    }

}
