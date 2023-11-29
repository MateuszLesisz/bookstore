package com.book_shop.bookstore.order.db;

import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime dateTime);
}
