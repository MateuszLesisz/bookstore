package com.book_shop.bookstore.order.domain;

import com.book_shop.bookstore.catalog.domain.Book;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderItem {
    Book book;
    int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }
}
