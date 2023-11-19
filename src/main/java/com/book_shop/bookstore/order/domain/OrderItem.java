package com.book_shop.bookstore.order.domain;

import com.book_shop.bookstore.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderItem {

    private Book book;
    int quantity;
}
