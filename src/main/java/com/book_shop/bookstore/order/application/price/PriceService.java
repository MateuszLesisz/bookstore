package com.book_shop.bookstore.order.application.price;

import com.book_shop.bookstore.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {
    private final List<DiscountStrategy> discountStrategies = List.of(
            new DeliveryDiscountStrategy(),
            new TotalPriceDiscountStrategy()
    );

    @Transactional
    public OrderPrice calculatePrice(Order order) {
        return new OrderPrice(
                order.getItemsPrice(),
                order.getDeliveryPrice(),
                discounts(order)
        );
    }

    private BigDecimal discounts(Order order) {
        return discountStrategies.stream()
                .map(strategy -> strategy.calculate(order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}