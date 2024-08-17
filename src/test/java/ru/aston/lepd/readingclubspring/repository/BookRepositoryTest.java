package ru.aston.lepd.readingclubspring.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.lepd.readingclubspring.config.AppConfig;
import ru.aston.lepd.readingclubspring.entity.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclubspring.Constants.*;


@Testcontainers
@Transactional
@Sql("classpath:data/data.sql")
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
class BookRepositoryTest {



    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private BookRepository bookRepository;


    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("datasource.url", container::getJdbcUrl);
    }

    @BeforeAll
    static void setUp() {
        container.start();
    }




    @Test
    public void findById_whenValidId_thenReturnOptionalOfBook() {
        final Long expectedId = 1L;
        final Long bookId = 1L;

        Optional<Book> actualResult = bookRepository.findById(bookId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }


    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long authorId = 666L;

        Optional<Book> actualResult = bookRepository.findById(authorId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Book> actualResult = bookRepository.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }


    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        bookRepository.deleteAll();
        List<Book> actualResult = bookRepository.findAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAllByReaderId_whenExist_thenReturnList() {
        final Long readerId = 1L;

        List<Book> actualResult = bookRepository.findAllByReaderId(readerId);

        assertFalse(actualResult.isEmpty());
        assertEquals(1, actualResult.size());
    }


    @Test
    public void findAllByReaderId_whenNotExist_thenReturnEmptyList() {
        final Long readerId = 1L;
        bookRepository.deleteById(1L);

        List<Book> actualResult = bookRepository.findAllByReaderId(readerId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAllByAuthors_Id_whenExist_thenReturnList() {
        final Long authorId = 3L;

        List<Book> actualResult = bookRepository.findAllByAuthors_Id(authorId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }


    @Test
    public void findAllByAuthors_Id_whenNotExist_thenReturnEmptyList() {
        final Long authorId = 3L;
        bookRepository.deleteById(1L);
        bookRepository.deleteById(3L);

        List<Book> actualResult = bookRepository.findAllByAuthors_Id(authorId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() {
        final Book book = new Book();
        book.setTitle("new title");
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3));
        book.setReader(SHORT_READER_1);

        Book saved = bookRepository.save(book);

        assertEquals(4L, saved.getId());
        assertEquals(book.getTitle(), saved.getTitle());
        assertEquals(book.getInventoryNumber(), saved.getInventoryNumber());
        assertEquals(2, saved.getAuthors().size());
        assertEquals(1L, saved.getReader().getId());
    }



    @Test
    public void deleteById_whenValidId_thenSuccess() {
        final Long bookId = 1L;

        bookRepository.deleteById(bookId);

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        assertTrue(optionalBook.isEmpty());
    }


    @Test
    void deleteById_whenInvalidId_thenException() {
        final Long bookId = 666L;

        assertThrows(EmptyResultDataAccessException.class, () -> bookRepository.deleteById(bookId));
    }



    @Test
    void deleteAllByReaderId_whenExist_thenSuccess() {
        final Long readerId = 1L;

        bookRepository.deleteAllByReaderId(readerId);

        assertEquals(2, bookRepository.findAll().size());
    }


    @Test
    void deleteAllByReaderId_whenNotExist_thenNothing() {
        final Long readerId = 666L;

        bookRepository.deleteAllByReaderId(readerId);

        assertEquals(3, bookRepository.findAll().size());
    }



    @Test
    void existsById_whenValidId_thenTrue() {
        final Long bookId = 1L;

        assertTrue(bookRepository.existsById(bookId));
    }


    @Test
    void existsById_whenInvalidId_thenFalse() {
        final Long bookId = 666L;

        assertFalse(bookRepository.existsById(bookId));
    }





    @AfterAll
    static void afterAll() throws SQLException {
        container.stop();
    }



}