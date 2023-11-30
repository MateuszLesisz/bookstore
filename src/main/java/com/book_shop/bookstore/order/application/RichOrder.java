package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class RichOrder {
    private Long id;
    private OrderStatus status;
    private Set<OrderItem> items;
    private Recipient recipient;
    private LocalDateTime createdAt;

    public BigDecimal totalPrice() {
        return items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
