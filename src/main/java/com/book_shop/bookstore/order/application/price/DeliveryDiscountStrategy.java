package com.book_shop.bookstore.order.application.price;

import com.book_shop.bookstore.order.domain.Order;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

public class DeliveryDiscountStrategy implements DiscountStrategy {

    public static final BigDecimal THRESHOLD = BigDecimal.valueOf(100);
    @Override
    public BigDecimal calculate(Order order) {
        if (order.getItemsPrice().compareTo(THRESHOLD) >= 0) {
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}