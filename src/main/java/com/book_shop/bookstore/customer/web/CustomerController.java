package com.book_shop.bookstore.customer.web;

import com.book_shop.bookstore.customer.domain.Customer;
import com.book_shop.bookstore.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/user")
    public Customer getCustomerDetails(Authentication authentication) {
        return customerService.getCustomerDetailsAfterLogin(authentication);
    }
}
