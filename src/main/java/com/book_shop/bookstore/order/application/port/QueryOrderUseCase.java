package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findAll();
}
