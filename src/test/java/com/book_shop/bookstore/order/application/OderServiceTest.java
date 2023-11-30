package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.db.BookJpaRepository;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookRepository;
    @Autowired
    ManipulateOrderService orderService;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    QueryOrderUseCase queryOrderUseCase;

    @Test
    public void userCanPlaceOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Book jcip = givenJavaConcurrency(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 10))
                .orderItem(new OrderItemCommand(jcip.getId(), 10))
                .build();

        //when
        PlaceOrderResponse response = orderService.placeOrder(command);

        //then
        assertTrue(response.isSuccess());
        assertEquals(40L, availableCopiesOf(effectiveJava));
        assertEquals(40L, availableCopiesOf(jcip));
    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {
        //given
        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 10))
                .build();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(command);
        });

        //then
        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested"));
    }

    @Test
    public void userCanRevokeOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));

        //when
        orderService.updateOrderStatus(orderId, OrderStatus.CANCELED);

        //then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    public void userCannotRevokePaidOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        orderService.updateOrderStatus(orderId, OrderStatus.PAID);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(orderId, OrderStatus.ABANDONED);
        });

        //then
        assertEquals(exception.getMessage(), "Unable to mark " + OrderStatus.PAID + " order as " + OrderStatus.ABANDONED);

    }


    private Book givenJavaConcurrency(long available) {
        return bookRepository.save(new Book("Java concurrency in Practice", 2006, new BigDecimal("99.90"), available));
    }

    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }

    private Recipient recipient() {
        return Recipient.builder().email("john@example.org").build();
    }

    public Long placeOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(bookId, copies))
                .build();
        return orderService.placeOrder(command).getRight();
    }

    private Long availableCopiesOf(Book book) {
        return catalogUseCase.findById(book.getId()).get().getAvailable();
    }
}