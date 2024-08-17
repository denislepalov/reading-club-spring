package ru.aston.lepd.readingclubspring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.aston.lepd.readingclubspring.dto.AuthorDto;
import ru.aston.lepd.readingclubspring.dto.BookDto;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.service.BookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;
import static ru.aston.lepd.readingclubspring.Constants.AUTHOR_DTO_1;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private static final String UPDATE_STRING = "Book with id=1 was updated";
    private static final String DELETE_STRING = "Book with id=1 was deleted";
    private static final String DELETE_ALL_BY_READER_ID_STRING = "All books by reader with id=1 was deleted";





    @Test
    void getById_whenValidId_thenBookDto() {
        final Long bookId = 1L;
        doReturn(BOOK_DTO_1).when(bookService).getById(bookId);

        ResponseEntity<BookDto> actualResult = bookController.getById(bookId);

        verify(bookService).getById(bookId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(BOOK_DTO_1, actualResult.getBody());
    }

    @Test
    void getById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doThrow(NotFoundException.class).when(bookService).getById(bookId);

        assertThrows(NotFoundException.class, () -> bookController.getById(bookId));
    }



    @Test
    void getAll_shouldReturnBookDtoList() {
        final List<BookDto> bookDtoList = List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3);
        Mockito.doReturn(bookDtoList).when(bookService).getAll();

        ResponseEntity<List<BookDto>> actualResult = bookController.getAll();

        verify(bookService).getAll();
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(bookDtoList, actualResult.getBody());
    }



    @Test
    void getAllByReaderId_whenValidReaderId_thenBookDtoList() {
        final Long readerId = 1L;
        final List<BookDto> bookDtoList = List.of(BOOK_DTO_1);
        Mockito.doReturn(bookDtoList).when(bookService).getAllByReaderId(readerId);

        ResponseEntity<List<BookDto>> actualResult = bookController.getAllByReaderId(readerId);

        verify(bookService).getAllByReaderId(readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(bookDtoList, actualResult.getBody());
    }

    @Test
    void getAllByReaderId_whenInvalidReaderId_thenEmptyBookDtoList() {
        final Long readerId = 666L;
        final List<BookDto> bookDtoList = new ArrayList<>(Collections.emptyList());
        Mockito.doReturn(bookDtoList).when(bookService).getAllByReaderId(readerId);

        ResponseEntity<List<BookDto>> actualResult = bookController.getAllByReaderId(readerId);

        verify(bookService).getAllByReaderId(readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(bookDtoList, actualResult.getBody());
    }




    @Test
    void getAllByAuthorId_whenValidAuthorId_thenBookDtoList() {
        final Long authorId = 1L;
        final List<BookDto> bookDtoList = List.of(BOOK_DTO_1);
        Mockito.doReturn(bookDtoList).when(bookService).getAllByAuthorId(authorId);

        ResponseEntity<List<BookDto>> actualResult = bookController.getAllByAuthorId(authorId);

        verify(bookService).getAllByAuthorId(authorId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(bookDtoList, actualResult.getBody());
    }

    @Test
    void getAllByAuthorId_whenInvalidAuthorId_thenEmptyBookDtoList() {
        final Long authorId = 666L;
        final List<BookDto> bookDtoList = new ArrayList<>(Collections.emptyList());
        Mockito.doReturn(bookDtoList).when(bookService).getAllByReaderId(authorId);

        ResponseEntity<List<BookDto>> actualResult = bookController.getAllByReaderId(authorId);

        verify(bookService).getAllByReaderId(authorId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(bookDtoList, actualResult.getBody());
    }



    @Test
    void save_shouldReturnBookDto() {
        doReturn(BOOK_DTO_1).when(bookService).save(BOOK_DTO_1);

        ResponseEntity<BookDto> actualResult = bookController.save(BOOK_DTO_1);

        verify(bookService).save(BOOK_DTO_1);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(BOOK_DTO_1, actualResult.getBody());
    }



    @Test
    void update_whenValidId_thenUpdateString() {
        final Long bookId = 1L;
        doReturn(BOOK_DTO_1).when(bookService).update(BOOK_DTO_1, bookId);

        ResponseEntity<String> actualResult = bookController.update(BOOK_DTO_1, bookId);

        verify(bookService).update(BOOK_DTO_1, bookId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(UPDATE_STRING, actualResult.getBody());
    }

    @Test
    void update_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doThrow(NotFoundException.class).when(bookService).update(BOOK_DTO_1, bookId);

        assertThrows(NotFoundException.class, () -> bookController.update(BOOK_DTO_1, bookId));

        verify(bookService).update(BOOK_DTO_1, bookId);
    }



    @Test
    void delete_whenValidId_thenDeleteString() {
        final Long bookId = 1L;
        doNothing().when(bookService).delete(bookId);

        ResponseEntity<String> actualResult = bookController.delete(bookId);

        verify(bookService).delete(bookId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(DELETE_STRING, actualResult.getBody());
    }

    @Test
    void delete_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doThrow(NotFoundException.class).when(bookService).delete(bookId);

        assertThrows(NotFoundException.class, () -> bookController.delete(bookId));

        verify(bookService).delete(bookId);
    }



    @Test
    void deleteAllByReaderId_whenValidId_thenDeleteString() {
        final Long readerId = 1L;
        doNothing().when(bookService).deleteAllByReaderId(readerId);

        ResponseEntity<String> actualResult = bookController.deleteAllByReaderId(readerId);

        verify(bookService).deleteAllByReaderId(readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(DELETE_ALL_BY_READER_ID_STRING, actualResult.getBody());
    }

    @Test
    void deleteAllByReaderId_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doThrow(NotFoundException.class).when(bookService).deleteAllByReaderId(readerId);

        assertThrows(NotFoundException.class, () -> bookController.deleteAllByReaderId(readerId));

        verify(bookService).deleteAllByReaderId(readerId);
    }



}