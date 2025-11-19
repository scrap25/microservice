// src/main/java/com/orderservice/kafka/producer/OrderEventProducer.java
package com.example.orderservice.kafka.producer;

import com.example.orderservice.config.KafkaConfig;
import com.example.orderservice.dto.OrderEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderEventDto> kafkaTemplate;

    public void sendOrderEvent(OrderEventDto orderEvent) {
        try {
            String key = orderEvent.getOrderId() != null ? orderEvent.getOrderId().toString() : "unknown";

            CompletableFuture<SendResult<String, OrderEventDto>> future =
                    kafkaTemplate.send(KafkaConfig.ORDER_EVENTS_TOPIC, key, orderEvent);

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Sent order event: {} with key: {} to topic: {} at offset: {}",
                            orderEvent.getEventType(), key, KafkaConfig.ORDER_EVENTS_TOPIC,
                            result.getRecordMetadata().offset());
                } else {
                    logger.error("Failed to send order event: {} with key: {} to topic: {}",
                            orderEvent.getEventType(), key, KafkaConfig.ORDER_EVENTS_TOPIC, exception);
                }
            });

        } catch (Exception e) {
            logger.error("Error sending order event to Kafka", e);
        }
    }
}