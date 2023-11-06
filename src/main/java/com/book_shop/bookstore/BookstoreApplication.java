package com.book_shop.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {


	private final CatalogService service;

	public BookstoreApplication(CatalogService catalogService) {
		this.service = catalogService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//CatalogService service = new CatalogService();
		List<Book> books = service.findByTitle("Pan Tadeusz");
		books.forEach(System.out::println);
	}
}
