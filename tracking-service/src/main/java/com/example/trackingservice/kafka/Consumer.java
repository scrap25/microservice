package com.example.trackingservice.kafka;

import com.example.trackingservice.service.TrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TrackingEventConsumer.class);

    @Autowired
    private TrackingService trackingService;

    @KafkaListener(topics = "order-events", groupId = "tracking-group")
    public void handleOrderEvent(String orderEvent) {
        logger.info("Received order event for tracking: {}", orderEvent);
        try {
            // Parse order event and create tracking record
            trackingService.createTrackingFromOrderEvent(orderEvent);
        } catch (Exception e) {
            logger.error("Error processing order event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-events", groupId = "tracking-group")
    public void handlePaymentEvent(String paymentEvent) {
        logger.info("Received payment event for tracking: {}", paymentEvent);
        try {
            // Update tracking status based on payment
            trackingService.updateTrackingFromPaymentEvent(paymentEvent);
        } catch (Exception e) {
            logger.error("Error processing payment event: {}", e.getMessage());
        }
    }
}
