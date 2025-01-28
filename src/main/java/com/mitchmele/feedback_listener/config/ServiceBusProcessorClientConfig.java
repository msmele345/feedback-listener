package com.mitchmele.feedback_listener.config;

import com.azure.messaging.servicebus.*;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class ServiceBusProcessorClientConfig {

    @Value("${messaging.servicebus.connectionstring}")
    private String connectionString;

    @Bean
    public ServiceBusProcessorClient screensBusProcessor() {
        ServiceBusClientBuilder builder = new ServiceBusClientBuilder();
        return builder
                .connectionString(connectionString)
                .processor()
                .processError(
                        (errorContext) ->
                                log.info("PROCESSOR ERROR: {}", errorContext.getException().getMessage()))
                .processMessage(ServiceBusProcessorClientConfig::processMessage)
                .topicName("bustopic2932")
                .subscriptionName("feedback")
                .buildProcessorClient();
    }

    @Bean
    public ServiceBusReceiverAsyncClient screensFeedbackConsumer() {
        ServiceBusClientBuilder builder = new ServiceBusClientBuilder();
        return builder
                .connectionString(connectionString)
                .receiver()
                .topicName("bustopic2932")
                .receiveMode(ServiceBusReceiveMode.RECEIVE_AND_DELETE)
                .subscriptionName("feedback")
                .buildAsyncClient();
    }

    @Bean
    public ServiceBusReceiverAsyncClient screensUserEventConsumer() {
        ServiceBusClientBuilder builder = new ServiceBusClientBuilder();
        return builder
                .connectionString(connectionString)
                .receiver()
                .topicName("bustopic2932")
                .receiveMode(ServiceBusReceiveMode.RECEIVE_AND_DELETE)
                .subscriptionName("UserEvent")
                .buildAsyncClient();
    }

    private static void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        log.info("PROCESSED MESSAGE. MSG:  {}, SEQUENCE: {}", message.getBody(), message.getSequenceNumber());
    }
}

