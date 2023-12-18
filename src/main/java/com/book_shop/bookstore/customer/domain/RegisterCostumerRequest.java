package com.book_shop.bookstore.customer.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterCostumerRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}