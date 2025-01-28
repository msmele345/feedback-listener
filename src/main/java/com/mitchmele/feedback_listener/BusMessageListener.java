package com.mitchmele.feedback_listener;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplicationShutdownHandlers;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusMessageListener { //this is a backend service for the screens UI.

    private final ServiceBusProcessorClient screensBusProcessor;

    @EventListener(ApplicationReadyEvent.class)
    public void listen() {
        screensBusProcessor.start();
    }

}
