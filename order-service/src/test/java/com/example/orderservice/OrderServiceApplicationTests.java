// src/test/java/com/orderservice/OrderServerApplicationTests.java
package com.example.orderservice;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "eureka.client.enabled=false"
})
class OrderServerApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertThat(orderService).isNotNull();
    }

    @Test
    void testCreateOrder() {
        OrderDto orderDto = new OrderDto("John Doe", "Laptop", 1, BigDecimal.valueOf(999.99));

        OrderDto createdOrder = orderService.createOrder(orderDto);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getCustomerName()).isEqualTo("John Doe");
        assertThat(createdOrder.getStatus()).isEqualTo(Order.OrderStatus.PENDING);
    }

    @Test
    void testGetOrderById() {
        OrderDto orderDto = new OrderDto("Jane Smith", "Phone", 2, BigDecimal.valueOf(599.99));
        OrderDto createdOrder = orderService.createOrder(orderDto);

        Optional<OrderDto> retrievedOrder = orderService.getOrderById(createdOrder.getId());

        assertThat(retrievedOrder).isPresent();
        assertThat(retrievedOrder.get().getCustomerName()).isEqualTo("Jane Smith");
    }

    @Test
    void testUpdateOrderStatus() {
        OrderDto orderDto = new OrderDto("Bob Johnson", "Tablet", 1, BigDecimal.valueOf(299.99));
        OrderDto createdOrder = orderService.createOrder(orderDto);

        OrderDto updatedOrder = orderService.updateOrderStatus(createdOrder.getId(), Order.OrderStatus.CONFIRMED);

        assertThat(updatedOrder.getStatus()).isEqualTo(Order.OrderStatus.CONFIRMED);
    }
}