// src/main/java/com/example/orderservice/dto/OrderEventDto.java
package com.example.orderservice.dto;

import com.example.orderservice.entity.Order;
import java.time.LocalDateTime;

public class OrderEventDto {
    private Long orderId;
    private String eventType;
    private String customerName;
    private String productName;
    private Integer quantity;
    private Order.OrderStatus status;
    private LocalDateTime timestamp;

    // Constructors
    public OrderEventDto() {}

    public OrderEventDto(Long orderId, String eventType, String customerName,
                         String productName, Integer quantity, Order.OrderStatus status) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Order.OrderStatus getStatus() { return status; }
    public void setStatus(Order.OrderStatus status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
