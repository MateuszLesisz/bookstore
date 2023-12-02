package com.book_shop.bookstore.order.application.price;

import com.book_shop.bookstore.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}