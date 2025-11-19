// src/main/java/com/orderservice/service/OrderService.java
package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    OrderDto updateOrderStatus(Long id, Order.OrderStatus status);
    Optional<OrderDto> getOrderById(Long id);
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByCustomerName(String customerName);
    List<OrderDto> getOrdersByStatus(Order.OrderStatus status);
    void deleteOrder(Long id);
}