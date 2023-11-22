package com.book_shop.bookstore.order.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    NEW, CONFIRMED, IN_DELIVERED, DELIVERED, RETURNED;

    public static Optional<OrderStatus> parseString(String value) {
        return Arrays.stream(values())
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }
}
