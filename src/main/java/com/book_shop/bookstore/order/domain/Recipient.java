package com.book_shop.bookstore.order.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Recipient {
    String name;
    String phone;
    String street;
    String city;
    String zipCode;
    String email;

    public Recipient(String name, String phone, String street, String city, String zipCode, String email) {
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.email = email;
    }
}
