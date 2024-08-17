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
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.service.AuthorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;


@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {


    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private static final String UPDATE_STRING = "Author with id=1 was updated";
    private static final String DELETE_STRING = "Author with id=1 was deleted";





    @Test
    void getAll_shouldReturnAuthorDtoList() {
        final List<AuthorDto> authorDtoList = List.of(AUTHOR_DTO_1, AUTHOR_DTO_2, AUTHOR_DTO_3);
        Mockito.doReturn(authorDtoList).when(authorService).getAll();

        ResponseEntity<List<AuthorDto>> actualResult = authorController.getAll();

        verify(authorService).getAll();
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(authorDtoList, actualResult.getBody());
    }



    @Test
    void getAllByBookId_whenValidBookId_thenAuthorDtoList() {
        final Long bookId = 1L;
        final List<AuthorDto> authorDtoList = List.of(AUTHOR_DTO_1, AUTHOR_DTO_3);
        Mockito.doReturn(authorDtoList).when(authorService).getAllByBookId(bookId);

        ResponseEntity<List<AuthorDto>> actualResult = authorController.getAllByBookId(bookId);

        verify(authorService).getAllByBookId(bookId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(authorDtoList, actualResult.getBody());
    }

    @Test
    void getAllByBookId_whenInvalidBookId_thenEmptyAuthorDtoList() {
        final Long bookId = 666L;
        final List<AuthorDto> authorDtoList = new ArrayList<>(Collections.emptyList());
        Mockito.doReturn(authorDtoList).when(authorService).getAllByBookId(bookId);

        ResponseEntity<List<AuthorDto>> actualResult = authorController.getAllByBookId(bookId);

        verify(authorService).getAllByBookId(bookId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(authorDtoList, actualResult.getBody());
    }



    @Test
    void getById_whenValidId_thenAuthorDto() {
        final Long authorId = 1L;
        doReturn(AUTHOR_DTO_1).when(authorService).getById(authorId);

        ResponseEntity<AuthorDto> actualResult = authorController.getById(authorId);

        verify(authorService).getById(authorId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(AUTHOR_DTO_1, actualResult.getBody());
    }

    @Test
    void getById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doThrow(NotFoundException.class).when(authorService).getById(authorId);

        assertThrows(NotFoundException.class, () -> authorController.getById(authorId));
    }



    @Test
    void save_shouldReturnAuthorDto() {
        doReturn(AUTHOR_DTO_1).when(authorService).save(AUTHOR_DTO_1);

        ResponseEntity<AuthorDto> actualResult = authorController.save(AUTHOR_DTO_1);

        verify(authorService).save(AUTHOR_DTO_1);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(AUTHOR_DTO_1, actualResult.getBody());
    }



    @Test
    void update_whenValidId_thenUpdateString() {
        final Long authorId = 1L;
        doReturn(AUTHOR_DTO_1).when(authorService).update(AUTHOR_DTO_1, authorId);

        ResponseEntity<String> actualResult = authorController.update(AUTHOR_DTO_1, authorId);

        verify(authorService).update(AUTHOR_DTO_1, authorId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(UPDATE_STRING, actualResult.getBody());
    }

    @Test
    void update_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doThrow(NotFoundException.class).when(authorService).update(AUTHOR_DTO_1, authorId);

        assertThrows(NotFoundException.class, () -> authorController.update(AUTHOR_DTO_1, authorId));

        verify(authorService).update(AUTHOR_DTO_1, authorId);
    }



    @Test
    void delete_whenValidId_thenDeleteString() {
        final Long authorId = 1L;
        doNothing().when(authorService).delete(authorId);

        ResponseEntity<String> actualResult = authorController.delete(authorId);

        verify(authorService).delete(authorId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(DELETE_STRING, actualResult.getBody());
    }

    @Test
    void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doThrow(NotFoundException.class).when(authorService).delete(authorId);

        assertThrows(NotFoundException.class, () -> authorController.delete(authorId));

        verify(authorService).delete(authorId);
    }


}