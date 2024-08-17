package ru.aston.lepd.readingclubspring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclubspring.dto.ReaderDto;
import ru.aston.lepd.readingclubspring.entity.Book;
import ru.aston.lepd.readingclubspring.entity.Reader;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.ReaderRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;


@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {


    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private ReaderService readerService;




    private Reader getReader() {
        Book book1 = new Book();
        book1.setId(1L);
        Reader reader = new Reader();
        reader.setId(1L);
        reader.setName("Ivan");
        reader.setSurname("Ivanov");
        reader.setPhone("71111111111");
        reader.setAddress("Lenina 11");
        READER_1.setBooks(List.of(book1));
        return reader;
    }

    @Test
    public void getById_whenValidId_thenReturnReaderDto() {
        final Long readerId = 1L;
        final Reader reader = getReader();
        doReturn(Optional.of(reader)).when(readerRepository).findById(readerId);
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(any(Reader.class));

        ReaderDto actualResult = readerService.getById(readerId);

        verify(readerRepository).findById(readerId);
        verify(mapper).readerToReaderDto(any(Reader.class));
        assertEquals(READER_DTO_1, actualResult);
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerRepository).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.getById(readerId));

        verify(readerRepository).findById(readerId);
        verify(mapper, never()).readerToReaderDto(any(Reader.class));
    }



    @Test
    public void getReaderById_whenValidId_thenReturnReader() {
        final Long readerId = 1L;
        final Reader reader = getReader();
        doReturn(Optional.of(reader)).when(readerRepository).findById(readerId);

        Reader actualResult = readerService.getReaderById(readerId);

        verify(readerRepository).findById(readerId);
        assertEquals(READER_1.getId(), actualResult.getId());
    }

    @Test
    public void getReaderById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerRepository).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.getReaderById(readerId));

        verify(readerRepository).findById(readerId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(getReader(), getReader(), getReader())).when(readerRepository).findAll();
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(any(Reader.class));

        List<ReaderDto> actualResult = readerService.getAll();

        verify(readerRepository).findAll();
        verify(mapper, times(3)).readerToReaderDto(any(Reader.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(readerRepository).findAll();

        List<ReaderDto> actualResult = readerService.getAll();

        verify(readerRepository).findAll();
        verify(mapper, never()).readerToReaderDto(any(Reader.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Reader reader = new Reader();
        reader.setName("Ivan");
        reader.setSurname("Ivanov");
        reader.setPhone("71111111111");
        reader.setAddress("Lenina 11");
        doReturn(reader).when(mapper).readerDtoToReader(READER_DTO_1);
        doReturn(SHORT_READER_1).when(readerRepository).save(reader);
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(SHORT_READER_1);

        ReaderDto actualResult = readerService.save(READER_DTO_1);

        verify(mapper).readerDtoToReader(READER_DTO_1);
        verify(readerRepository).save(reader);
        verify(mapper).readerToReaderDto(SHORT_READER_1);
        assertEquals(READER_DTO_1, actualResult);
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long readerId = 1L;
        final Reader reader = getReader();
        doReturn(Optional.of(reader)).when(readerRepository).findById(readerId);
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(reader);

        ReaderDto actualResult = readerService.update(READER_DTO_1, readerId);

        verify(readerRepository).findById(readerId);
        verify(mapper).readerToReaderDto(reader);
        assertEquals(READER_DTO_1, actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerRepository).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.update(READER_DTO_1, readerId));

        verify(readerRepository).findById(readerId);
        verify(mapper, never()).readerToReaderDto(any(Reader.class));
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long readerId = 1L;
        doReturn(true).when(readerRepository).existsById(readerId);
        doNothing().when(readerRepository).deleteById(readerId);

        readerService.delete(readerId);

        verify(readerRepository).existsById(readerId);
        verify(readerRepository).deleteById(readerId);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(false).when(readerRepository).existsById(authorId);

        assertThrows(NotFoundException.class, () -> readerService.delete(authorId));

        verify(readerRepository).existsById(authorId);
        verify(readerRepository, never()).deleteById(authorId);
    }



    @Test
    public void existsById_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(readerRepository).existsById(authorId);

        boolean actualResult = readerService.existsById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void existsById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(readerRepository).existsById(authorId);

        assertThrows(NotFoundException.class, () -> readerService.existsById(authorId));
    }


  
}