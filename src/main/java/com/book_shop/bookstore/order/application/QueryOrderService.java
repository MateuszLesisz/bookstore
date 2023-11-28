package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.catalog.db.BookJpaRepository;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.db.OrderJpaRepository;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final BookJpaRepository bookRepository;

    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RichOrder> findById(Long id) {
        return orderRepository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {
        List<RichOrderItem> richItems = toRichItems(order.getItems());
        return new RichOrder(order.getId(), order.getStatus(), richItems, order.getRecipient(), order.getCreatedAt());
    }

    private List<RichOrderItem> toRichItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Book book = bookRepository.findById(item.getId()).orElseThrow(() -> new IllegalStateException("Unable to find book with ID: " + item.getBookId()));
                    return new RichOrderItem(book, item.getQuantity());
                })
                .collect(Collectors.toList());
    }
}