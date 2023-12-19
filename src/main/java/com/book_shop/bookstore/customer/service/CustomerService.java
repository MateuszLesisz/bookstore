package com.book_shop.bookstore.customer.service;

import com.book_shop.bookstore.customer.db.CustomerRepository;
import com.book_shop.bookstore.customer.domain.Customer;
import com.book_shop.bookstore.customer.domain.RegisterCostumerRequest;
import com.book_shop.bookstore.exception.UserAlreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> registerUser(RegisterCostumerRequest request) {
        if (customerRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistException("User with given email already exist.", HttpStatus.CONFLICT);
        } else {
            customerRepository.save(createUser(request));
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User successfully registered.");
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
}
