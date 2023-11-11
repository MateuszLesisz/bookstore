package com.book_shop.bookstore.order.domain;

import lombok.Value;

@Value
public class Recipient {
    String name;
    String phone;
    String street;
    String city;
    String zipCode;
    String email;
}
