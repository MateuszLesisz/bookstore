package com.book_shop.bookstore.order;

import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase;
import com.book_shop.bookstore.order.db.OrderJpaRepository;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AbandonedOrderJob {
    private final OrderJpaRepository orderRepository;
    private final ManipulateOrderUseCase orderUseCase;

    @Scheduled(fixedRate = 60_000L)
    @Transactional
    public void run() {
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(30);
        List<Order> orders = orderRepository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, timestamp);
        System.out.println("Found orders to be abandoned: " + orders.size());
        orders.forEach(order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));
    }
}