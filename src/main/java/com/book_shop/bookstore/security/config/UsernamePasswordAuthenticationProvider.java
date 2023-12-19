package com.book_shop.bookstore.security.config;


import com.book_shop.bookstore.customer.db.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<GrantedAuthority> authorities = new ArrayList<>();
        return customerRepository.findByEmail(username)
                .filter(customer -> passwordEncoder.matches(pwd, customer.getPwd()))
                .map(customer -> {
                    authorities.add(new SimpleGrantedAuthority(customer.getRole()));
                    return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}