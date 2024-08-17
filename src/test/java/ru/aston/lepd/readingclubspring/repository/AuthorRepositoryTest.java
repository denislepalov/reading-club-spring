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
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@Transactional
@Sql("classpath:data/data.sql")
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
class AuthorRepositoryTest {


    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private AuthorRepository authorRepository;


    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("datasource.url", container::getJdbcUrl);
    }

    @BeforeAll
    static void setUp() {
        container.start();
    }






    @Test
    public void findById_whenValidId_thenReturnOptionalOfAuthor() {
        final Long expectedId = 1L;
        final Long authorId = 1L;

        Optional<Author> actualResult = authorRepository.findById(authorId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }


    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long authorId = 666L;

        Optional<Author> actualResult = authorRepository.findById(authorId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Author> actualResult = authorRepository.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }


    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        authorRepository.deleteAll();

        List<Author> actualResult = authorRepository.findAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAuthorsByBooks_Id_whenExist_thenReturnList() {
        final Long bookId = 1L;

        List<Author> actualResult = authorRepository.findAuthorsByBooks_Id(bookId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }


    @Test
    public void findAuthorsByBooks_Id_whenNotExist_thenReturnEmptyList() {
        final Long bookId = 666L;

        List<Author> actualResult = authorRepository.findAuthorsByBooks_Id(bookId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() {
        final Book book = new Book();
        book.setId(1L);
        final Author author = new Author();
        author.setFullName("Steven King");
        author.setPersonalInfo("likes fish");
        author.setBooks(List.of(book));

        Author saved = authorRepository.save(author);

        assertEquals(4L, saved.getId());
        assertEquals(author.getFullName(), saved.getFullName());
        assertEquals(author.getPersonalInfo(), saved.getPersonalInfo());
        assertEquals(1, author.getBooks().size());
    }



    @Test
    public void deleteById_whenValidId_thenSuccess() {
        final Long authorId = 1L;

        authorRepository.deleteById(authorId);

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        assertTrue(optionalAuthor.isEmpty());
    }


    @Test
    void deleteById_whenInvalidId_thenException() {
        final Long bookId = 666L;

        assertThrows(EmptyResultDataAccessException.class, () -> authorRepository.deleteById(bookId));
    }



    @Test
    void existsById_whenValidId_thenTrue() {
        final Long authorId = 1L;

        assertTrue(authorRepository.existsById(authorId));
    }


    @Test
    void existsById_whenInvalidId_thenFalse() {
        final Long authorId = 666L;

        assertFalse(authorRepository.existsById(authorId));
    }





    @AfterAll
    static void afterAll() throws SQLException {
        container.stop();
    }


}