package com.book_shop.bookstore.order.domain;

import com.book_shop.bookstore.catalog.domain.Book;
import lombok.Value;

@Value
public class OrderItem {
    Book book;
    int quantity;
}
