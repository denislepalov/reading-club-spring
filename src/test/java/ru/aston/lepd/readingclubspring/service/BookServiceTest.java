package ru.aston.lepd.readingclubspring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclubspring.dto.BookDto;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;
import ru.aston.lepd.readingclubspring.entity.Reader;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.BookRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {


    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorService authorService;
    @Mock
    private ReaderService readerService;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private BookService bookService;




    @Test
    public void getById_whenValidId_thenReturnBookDto() {
        final Long bookId = 1L;
        doReturn(Optional.of(BOOK_1)).when(bookRepository).findById(bookId);
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        BookDto actualResult = bookService.getById(bookId);

        verify(bookRepository).findById(bookId);
        verify(mapper).bookToBookDto(any(Book.class));
        assertEquals(BOOK_DTO_1.getInventoryNumber(), actualResult.getInventoryNumber());
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookRepository).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.getById(bookId));

        verify(bookRepository).findById(bookId);
        verify(mapper, never()).bookToBookDto(any(Book.class));
    }



    @Test
    public void getBookById_whenValidId_thenReturnBook() {
        final Long bookId = 1L;
        doReturn(Optional.of(BOOK_1)).when(bookRepository).findById(bookId);

        Book actualResult = bookService.getBookById(bookId);

        verify(bookRepository).findById(bookId);
        assertEquals(BOOK_1.getId(), actualResult.getId());
    }

    @Test
    public void getBookById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookRepository).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.getBookById(bookId));

        verify(bookRepository).findById(bookId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(SHORT_BOOK_1, SHORT_BOOK_2, SHORT_BOOK_3)).when(bookRepository).findAll();
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAll();

        verify(bookRepository).findAll();
        verify(mapper, times(3)).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(bookRepository).findAll();

        List<BookDto> actualResult = bookService.getAll();

        verify(bookRepository).findAll();
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    void getAllByReaderId_whenExist_thenReturnList() {
        final Long readerId = 1L;
        doReturn(List.of(SHORT_BOOK_1)).when(bookRepository).findAllByReaderId(readerId);
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        verify(bookRepository).findAllByReaderId(readerId);
        verify(mapper).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(1, actualResult.size());
    }

    @Test
    void getAllByReaderId_whenNotExist_thenReturnEmptyList() {
        final Long readerId = 666L;
        doReturn(Collections.emptyList()).when(bookRepository).findAllByReaderId(readerId);

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        verify(bookRepository).findAllByReaderId(readerId);
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    void getAllByAuthorId_whenExist_thenReturnList() {
        final Long authorId = 3L;
        doReturn(List.of(SHORT_BOOK_1, SHORT_BOOK_3)).when(bookRepository).findAllByAuthors_Id(authorId);
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        verify(bookRepository).findAllByAuthors_Id(authorId);
        verify(mapper, times(2)).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    void getAllByAuthorId_whenNotExist_thenReturnEmptyList() {
        final Long authorId = 666L;
        doReturn(Collections.emptyList()).when(bookRepository).findAllByAuthors_Id(authorId);

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        verify(bookRepository).findAllByAuthors_Id(authorId);
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Long expectedInventoryNumber = 22222L;
        final Book book = getBook();
        doReturn(book).when(mapper).bookDtoToBook(BOOK_DTO_2);
        doReturn(AUTHOR_2).when(authorService).getAuthorById(anyLong());
        doReturn(READER_2).when(readerService).getReaderById(anyLong());
        doReturn(BOOK_2).when(bookRepository).save(book);
        doReturn(BOOK_DTO_2).when(mapper).bookToBookDto(BOOK_2);

        BookDto actualResult = bookService.save(BOOK_DTO_2);

        verify(mapper).bookDtoToBook(BOOK_DTO_2);
        verify(authorService).getAuthorById(anyLong());
        verify(readerService).getReaderById(anyLong());
        verify(bookRepository).save(book);
        verify(mapper).bookToBookDto(BOOK_2);
        assertEquals(expectedInventoryNumber, actualResult.getInventoryNumber());
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long expectedInventoryNumber = 11111L;
        final Long bookId = 1L;
        final Book book = getBook();
        doReturn(Optional.of(book)).when(bookRepository).findById(bookId);
        doReturn(AUTHOR_2).when(authorService).getAuthorById(anyLong());
        doReturn(READER_2).when(readerService).getReaderById(anyLong());
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(book);

        BookDto actualResult = bookService.update(BOOK_DTO_1, bookId);

        verify(bookRepository).findById(bookId);
        verify(authorService, times(2)).getAuthorById(anyLong());
        verify(readerService).getReaderById(anyLong());
        assertEquals(expectedInventoryNumber, actualResult.getInventoryNumber());
    }

    @Test
    public void update_whenValidIdAndAuthorsAreEmpty_thenSuccess() {
        final Long expectedInventoryNumber = 11111L;
        final Long bookId = 1L;
        final Book book = getBook();
        final BookDto bookDto = getBookDto();
        bookDto.setAuthorIds(new ArrayList<>());
        doReturn(Optional.of(book)).when(bookRepository).findById(bookId);
        doReturn(READER_2).when(readerService).getReaderById(anyLong());
        doReturn(bookDto).when(mapper).bookToBookDto(book);

        BookDto actualResult = bookService.update(bookDto, bookId);

        verify(bookRepository).findById(bookId);
        verify(readerService).getReaderById(anyLong());
        assertEquals(expectedInventoryNumber, actualResult.getInventoryNumber());

    }

    @Test
    public void update_whenValidIdAndReaderIsNull_thenSuccess() {
        final Long expectedInventoryNumber = 11111L;
        final Long bookId = 1L;
        final Book book = getBook();
        final BookDto bookDto = getBookDto();
        bookDto.setReaderId(null);
        doReturn(Optional.of(book)).when(bookRepository).findById(bookId);
        doReturn(AUTHOR_2).when(authorService).getAuthorById(anyLong());
        doReturn(bookDto).when(mapper).bookToBookDto(book);

        BookDto actualResult = bookService.update(bookDto, bookId);

        verify(bookRepository).findById(bookId);
        verify(authorService, times(2)).getAuthorById(anyLong());
        assertEquals(expectedInventoryNumber, actualResult.getInventoryNumber());

    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookRepository).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.update(BOOK_DTO_1, bookId));

        verify(bookRepository).findById(bookId);
        verify(authorService, never()).getAuthorById(anyLong());
        verify(readerService, never()).getReaderById(anyLong());
    }



    @Test
    public void delete_whenValidId_thenSuccess() {
        final Long bookId = 1L;
        doReturn(true).when(bookRepository).existsById(bookId);
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.delete(bookId);

        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(false).when(bookRepository).existsById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.delete(bookId));

        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }



    @Test
    public void deleteAllByReaderId_whenValidId_thenSuccess() {
        final Long readerId = 1L;
        doReturn(true).when(readerService).existsById(readerId);
        doNothing().when(bookRepository).deleteAllByReaderId(readerId);

        bookService.deleteAllByReaderId(readerId);

        verify(readerService).existsById(readerId);
        verify(bookRepository).deleteAllByReaderId(readerId);
    }

    @Test
    public void deleteAllByReaderId_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doThrow(NotFoundException.class).when(readerService).existsById(readerId);

        assertThrows(NotFoundException.class, () -> bookService.deleteAllByReaderId(readerId));

        verify(readerService).existsById(readerId);
        verify(bookRepository, never()).deleteAllByReaderId(readerId);
    }



    @Test
    public void existsById_whenValidId_thenTrue() {
        final Long bookId = 1L;
        doReturn(true).when(bookRepository).existsById(bookId);

        boolean actualResult = bookService.existsById(bookId);

        assertTrue(actualResult);
    }

    @Test
    public void existsById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(bookRepository).existsById(authorId);

        assertThrows(NotFoundException.class, () -> bookService.existsById(authorId));
    }



    private Book getBook() {
        final Reader reader = new Reader();
        reader.setId(2L);
        final Author author = new Author();
        author.setId(2L);
        final Book book = new Book();
        book.setTitle("Title2");
        book.setInventoryNumber(22222L);
        book.setAuthors(List.of(author));
        book.setReader(reader);
        return book;
    }

    private BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title1");
        bookDto.setInventoryNumber(11111L);
        bookDto.setAuthorIds(List.of(1L, 3L));
        bookDto.setReaderId(1L);
        return bookDto;
    }


}