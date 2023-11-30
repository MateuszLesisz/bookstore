package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);
}
