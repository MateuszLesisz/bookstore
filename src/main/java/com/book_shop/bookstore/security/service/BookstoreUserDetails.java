package com.book_shop.bookstore.security.service;

import com.book_shop.bookstore.customer.db.CustomerRepository;
import com.book_shop.bookstore.customer.domain.Customer;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookstoreUserDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String userName, password;
        List<GrantedAuthority> authorities = new ArrayList<>();
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isEmpty()) {
            throw new UsernameNotFoundException("User details not found for the user: " + email);
        } else {
            Customer customer = customerOptional.get();
            userName = customer.getEmail();
            password = customer.getPwd();
            authorities.add(new SimpleGrantedAuthority(customer.getRole()));
        }
        return new User(userName, password, authorities);
    }
}
