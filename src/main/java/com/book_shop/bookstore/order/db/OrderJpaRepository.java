package com.book_shop.bookstore.order.db;

import com.book_shop.bookstore.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
