// src/main/java/com/orderservice/service/impl/OrderServiceImpl.java
package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEventDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.producer.OrderEventProducer;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = convertToEntity(orderDto);
        Order savedOrder = orderRepository.save(order);

        // Send Kafka event
        OrderEventDto eventDto = new OrderEventDto(
                savedOrder.getId(),
                "ORDER_CREATED",
                savedOrder.getCustomerName(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getStatus()
        );
        orderEventProducer.sendOrderEvent(eventDto);

        return convertToDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setCustomerName(orderDto.getCustomerName());
            order.setProductName(orderDto.getProductName());
            order.setQuantity(orderDto.getQuantity());
            order.setPrice(orderDto.getPrice());

            Order savedOrder = orderRepository.save(order);

            // Send Kafka event
            OrderEventDto eventDto = new OrderEventDto(
                    savedOrder.getId(),
                    "ORDER_UPDATED",
                    savedOrder.getCustomerName(),
                    savedOrder.getProductName(),
                    savedOrder.getQuantity(),
                    savedOrder.getStatus()
            );
            orderEventProducer.sendOrderEvent(eventDto);

            return convertToDto(savedOrder);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public OrderDto updateOrderStatus(Long id, Order.OrderStatus status) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            Order.OrderStatus previousStatus = order.getStatus();
            order.setStatus(status);

            Order savedOrder = orderRepository.save(order);

            // Send Kafka event
            OrderEventDto eventDto = new OrderEventDto(
                    savedOrder.getId(),
                    "ORDER_STATUS_CHANGED",
                    savedOrder.getCustomerName(),
                    savedOrder.getProductName(),
                    savedOrder.getQuantity(),
                    savedOrder.getStatus()
            );
            orderEventProducer.sendOrderEvent(eventDto);

            return convertToDto(savedOrder);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerName(customerName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);

            // Send Kafka event
            OrderEventDto eventDto = new OrderEventDto(
                    id, "ORDER_DELETED", null, null, null, null
            );
            orderEventProducer.sendOrderEvent(eventDto);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setProductName(order.getProductName());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    private Order convertToEntity(OrderDto dto) {
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        return order;
    }
}