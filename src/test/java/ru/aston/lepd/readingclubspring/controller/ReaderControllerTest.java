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
import ru.aston.lepd.readingclubspring.dto.ReaderDto;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.service.ReaderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;
import static ru.aston.lepd.readingclubspring.Constants.AUTHOR_DTO_1;


@ExtendWith(MockitoExtension.class)
class ReaderControllerTest {

    @Mock
    private  ReaderService readerService;

    @InjectMocks
    private ReaderController readerController;

    private static final String UPDATE_STRING = "Reader with id=1 was updated";
    private static final String DELETE_STRING = "Reader with id=1 was deleted";




    @Test
    void getAll_shouldReturnReaderDtoList() {
        final List<ReaderDto> readerDtoList = List.of(READER_DTO_1, READER_DTO_2, READER_DTO_3);
        Mockito.doReturn(readerDtoList).when(readerService).getAll();

        ResponseEntity<List<ReaderDto>> actualResult = readerController.getAll();

        verify(readerService).getAll();
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(readerDtoList, actualResult.getBody());
    }



    @Test
    void getById_whenValidId_thenReaderDto() {
        final Long readerId = 1L;
        doReturn(READER_DTO_1).when(readerService).getById(readerId);

        ResponseEntity<ReaderDto> actualResult = readerController.getById(readerId);

        verify(readerService).getById(readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(READER_DTO_1, actualResult.getBody());
    }

    @Test
    void getById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doThrow(NotFoundException.class).when(readerService).getById(readerId);

        assertThrows(NotFoundException.class, () -> readerController.getById(readerId));
    }



    @Test
    void save_shouldReturnReaderDto() {
        doReturn(READER_DTO_1).when(readerService).save(READER_DTO_1);

        ResponseEntity<ReaderDto> actualResult = readerController.save(READER_DTO_1);

        verify(readerService).save(READER_DTO_1);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualResult.getHeaders().getContentType());
        assertEquals(READER_DTO_1, actualResult.getBody());
    }



    @Test
    void update_whenValidId_thenUpdateString() {
        final Long readerId = 1L;
        doReturn(READER_DTO_1).when(readerService).update(READER_DTO_1, readerId);

        ResponseEntity<String> actualResult = readerController.update(READER_DTO_1, readerId);

        verify(readerService).update(READER_DTO_1, readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(UPDATE_STRING, actualResult.getBody());
    }

    @Test
    void update_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doThrow(NotFoundException.class).when(readerService).update(READER_DTO_1, readerId);

        assertThrows(NotFoundException.class, () -> readerController.update(READER_DTO_1, readerId));

        verify(readerService).update(READER_DTO_1, readerId);
    }



    @Test
    void delete_whenValidId_thenDeleteString() {
        final Long readerId = 1L;
        doNothing().when(readerService).delete(readerId);

        ResponseEntity<String> actualResult = readerController.delete(readerId);

        verify(readerService).delete(readerId);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, actualResult.getHeaders().getContentType());
        assertEquals(DELETE_STRING, actualResult.getBody());
    }

    @Test
    void delete_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doThrow(NotFoundException.class).when(readerService).delete(readerId);

        assertThrows(NotFoundException.class, () -> readerController.delete(readerId));

        verify(readerService).delete(readerId);
    }


}