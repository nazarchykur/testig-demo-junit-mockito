package com.example.sbtestcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@Testcontainers
////@SpringBootTest
@DataJpaTest
class BookRepositoryTest_3_extendDatabaseTest_IT extends DatabaseTest {

    @Autowired
    public BookRepository bookRepository;

    @Test
    void contextLoads() {
        Book book = new Book();
        String bookName = "Testcontainers";
        book.setName(bookName);

        bookRepository.save(book);

        Book book1 = bookRepository.findAll().get(0);

        assertThat(book1.getId()).isNotNull();
        assertThat(book1.getName()).isEqualTo(bookName);

        System.out.println("Context loads!");
    }
}
