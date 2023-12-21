package com.book_shop.bookstore.security.web;

import com.book_shop.bookstore.customer.domain.RegisterCostumerRequest;
import com.book_shop.bookstore.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegisterCostumerRequest request) {
        customerService.registerUser(request);
    }

}
