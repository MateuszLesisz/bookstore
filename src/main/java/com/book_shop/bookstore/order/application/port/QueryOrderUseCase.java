package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<Order> findAll();
    Optional<Order> findById(Long id);
}
