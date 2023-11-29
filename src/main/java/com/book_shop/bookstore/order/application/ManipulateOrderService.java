package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.catalog.db.BookJpaRepository;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase;
import com.book_shop.bookstore.order.db.OrderJpaRepository;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
@Transactional
public class ManipulateOrderService implements ManipulateOrderUseCase {
    private final OrderJpaRepository orderRepository;
    private final BookJpaRepository bookRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getOrderItems().stream()
                .map(this::toOrderItem)
                .collect(toSet());
        Order order = Order.builder()
                .recipient(command.getRecipient())
                .items(items)
                .build();
        Order save = orderRepository.save(order);
        bookRepository.saveAll(updateBooks(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        Book book = bookRepository.findById(command.getBookId()).orElseThrow(RuntimeException::new);
        if (book.getAvailable() >= command.getQuantity()) {
            return new OrderItem(book, command.getQuantity());
        }
        throw new IllegalArgumentException("Too many copies of book " + book.getId() + " requested " + command.getQuantity() + " of " + book.getAvailable() + " available.");
    }

    private Set<Book> updateBooks(Set<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() - item.getQuantity());
                    return book;
                })
                .collect(toSet());
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderRepository.findById(id)
                .ifPresent(order -> {
                    order.updateStatus(status);
                    orderRepository.save(order);
                });
    }
}