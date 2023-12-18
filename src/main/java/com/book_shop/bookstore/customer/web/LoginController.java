package com.book_shop.bookstore.customer.web;

import com.book_shop.bookstore.customer.domain.RegisterCostumerRequest;
import com.book_shop.bookstore.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/register")
public class LoginController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> registerUser(@RequestBody RegisterCostumerRequest request) {
    return customerService.registerUser(request);
    }

}
