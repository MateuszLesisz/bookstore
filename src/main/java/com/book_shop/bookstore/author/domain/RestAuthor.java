package com.book_shop.bookstore.author.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public record RestAuthor(String name) {
}