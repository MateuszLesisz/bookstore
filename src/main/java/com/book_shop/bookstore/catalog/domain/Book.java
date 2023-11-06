package com.book_shop.bookstore.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class Book {

    private final Long id;
    private final String title;
    private final String author;
    private final Integer year;

}
