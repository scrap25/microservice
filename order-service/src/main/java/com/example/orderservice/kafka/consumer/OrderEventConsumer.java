package com.example.orderservice.kafka.consumer;

import com.example.orderservice.config.KafkaConfig;
import com.example.orderservice.dto.OrderEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = KafkaConfig.ORDER_EVENTS_TOPIC, groupId = "order-service-group")
    public void consumeOrderEvent(@Payload OrderEventDto orderEvent,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                  @Header(KafkaHeaders.OFFSET) long offset) {

        try {
            logger.info("Received order event: {} for order ID: {} from topic: {}, partition: {}, offset: {}",
                    orderEvent.getEventType(), orderEvent.getOrderId(), topic, partition, offset);

            // Process the order event
            processOrderEvent(orderEvent);

        } catch (Exception e) {
            logger.error("Error processing order event: {}", orderEvent, e);
        }
    }

    private void processOrderEvent(OrderEventDto orderEvent) {
        // Add your business logic here
        switch (orderEvent.getEventType()) {
            case "ORDER_CREATED":
                logger.info("Processing order creation for customer: {}", orderEvent.getCustomerName());
                break;
            case "ORDER_UPDATED":
                logger.info("Processing order update for order ID: {}", orderEvent.getOrderId());
                break;
            case "ORDER_STATUS_CHANGED":
                logger.info("Processing status change for order ID: {} to status: {}",
                        orderEvent.getOrderId(), orderEvent.getStatus());
                break;
            case "ORDER_DELETED":
                logger.info("Processing order deletion for order ID: {}", orderEvent.getOrderId());
                break;
            default:
                logger.warn("Unknown order event type: {}", orderEvent.getEventType());
        }
    }
}