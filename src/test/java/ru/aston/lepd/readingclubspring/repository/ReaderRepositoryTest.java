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
import ru.aston.lepd.readingclubspring.entity.Reader;

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
class ReaderRepositoryTest {



    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private ReaderRepository readerRepository;

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
    public void findById_whenValidId_thenReturnOptionalOfReader() {
        final Long expectedId = 1L;
        final Long readerId = 1L;

        Optional<Reader> actualResult = readerRepository.findById(readerId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }

    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long readerId = 666L;

        Optional<Reader> actualResult = readerRepository.findById(readerId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Reader> actualResult = readerRepository.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        bookRepository.deleteAll();
        readerRepository.deleteAll();

        List<Reader> actualResult = readerRepository.findAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() {
        final Author author = new Author();
        author.setId(1L);
        final Book book = new Book();
        book.setTitle("Title");
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(author));
        final Reader reader = new Reader();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");
        reader.setBooks(List.of(book));

        Reader saved = readerRepository.save(reader);

        assertEquals(4L, saved.getId());
        assertEquals(reader.getName(), saved.getName());
        assertEquals(reader.getSurname(), saved.getSurname());
        assertEquals(reader.getPhone(), saved.getPhone());
        assertEquals(reader.getAddress(), saved.getAddress());
        assertEquals(reader.getBooks(), saved.getBooks());
    }




    @Test
    public void deleteById_whenValidId_thenSuccess() {
        final Long readerId = 1L;
        bookRepository.deleteAllByReaderId(readerId);

        readerRepository.deleteById(readerId);

        assertEquals(2, readerRepository.findAll().size());
    }

    @Test
    public void deleteById_whenInvalidId_thenException() {
        final Long readerId = 666L;

        assertThrows(EmptyResultDataAccessException.class, () -> readerRepository.deleteById(readerId));
    }



    @Test
    void existsById_whenValidKey_thenTrue() {
        final Long readerId = 1L;

        assertTrue(readerRepository.existsById(readerId));
    }

    @Test
    void existsById_whenInvalidKey_thenFalse() {
        final Long readerId = 666L;

        assertFalse(readerRepository.existsById(readerId));
    }








    @AfterAll
    static void afterAll() throws SQLException {
        container.stop();
    }



}