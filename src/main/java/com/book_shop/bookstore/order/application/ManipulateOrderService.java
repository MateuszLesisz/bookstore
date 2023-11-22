package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase;
import com.book_shop.bookstore.order.db.OrderJpaRepository;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManipulateOrderService implements ManipulateOrderUseCase {

    private final OrderJpaRepository orderRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order.builder()
                .recipient(command.getRecipient())
                .items(command.getOrderItems())
                .build();
        Order save = orderRepository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderRepository.findById(id)
                .ifPresent(order -> {
                    order.setStatus(status);
                    orderRepository.save(order);
                });
    }
}