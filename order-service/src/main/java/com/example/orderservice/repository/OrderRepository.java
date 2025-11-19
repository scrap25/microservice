// src/main/java/com/orderservice/repository/OrderRepository.java
package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerName(String customerName);

    List<Order> findByStatus(Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.customerName = :customerName AND o.status = :status")
    List<Order> findByCustomerNameAndStatus(@Param("customerName") String customerName,
                                            @Param("status") Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.productName LIKE %:productName%")
    List<Order> findByProductNameContaining(@Param("productName") String productName);
}