package com.book_shop.bookstore.order.domain;

import com.book_shop.bookstore.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipient extends BaseEntity {
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;
}
