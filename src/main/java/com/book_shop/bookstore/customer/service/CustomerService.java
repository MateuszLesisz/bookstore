package com.book_shop.bookstore.customer.service;

import com.book_shop.bookstore.customer.db.CustomerRepository;
import com.book_shop.bookstore.customer.domain.Customer;
import com.book_shop.bookstore.customer.domain.RegisterCostumerRequest;
import com.book_shop.bookstore.exception.UserAlreadyExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterCostumerRequest request) {
        if (customerRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistException("User with given email already exist.", HttpStatus.CONFLICT);
        } else {
            customerRepository.save(createUser(request));
            log.info("User successfully registered with email= {}", request.getEmail());
        }
    }

    private Customer createUser(RegisterCostumerRequest request) {
        return Customer.builder()
                .withEmail(request.getEmail())
                .withPwd(passwordEncoder.encode(request.getPassword()))
                .withRole(request.getRole())
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(null)
                .build();
    }

    public Customer getCustomerDetailsAfterLogin(Authentication authentication) {
        return customerRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}
