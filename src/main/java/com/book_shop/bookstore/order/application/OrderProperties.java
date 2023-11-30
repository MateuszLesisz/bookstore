package com.book_shop.bookstore.order.application;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Value
@ConfigurationProperties("app.orders")
public class OrderProperties {
    String abandonCron;
    Duration paymentPeriod;
}