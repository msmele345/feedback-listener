package com.mitchmele.feedback_listener.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
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
}

